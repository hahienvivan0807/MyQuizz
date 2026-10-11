package com.example.myquizz.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.myquizz.model.CauHoi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeminiAiHelper {

    private static final String TAG = "GeminiAiHelper";
    private static final String PREF_NAME = "gemini_ai_prefs";
    private static final String KEY_GEMINI_API_KEY = "gemini_api_key";

    // BẠN CÓ THỂ DÁN GEMINI API KEY TRỰC TIẾP VÀO ĐÂY NẾU MUỐN CÀI MẶC ĐỊNH TRONG CODE:
    public static final String DEFAULT_GEMINI_API_KEY = "";

    // Thử lần lượt: model chính quá tải (503) thì chuyển sang model nhẹ hơn
    private static final String[] GEMINI_MODELS = {
            "gemini-flash-latest",
            "gemini-flash-lite-latest",
            "gemini-3.5-flash-lite"
    };

    public interface AiCallback {
        void onSuccess(List<CauHoi> questions);
        void onError(String errorMessage);
    }

    public static String getApiKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedKey = prefs.getString(KEY_GEMINI_API_KEY, "");
        if (savedKey.trim().isEmpty() && !DEFAULT_GEMINI_API_KEY.trim().isEmpty()) {
            return DEFAULT_GEMINI_API_KEY.trim();
        }
        return savedKey;
    }

    public static void saveApiKey(Context context, String apiKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_GEMINI_API_KEY, apiKey.trim()).apply();
    }

    public static void generateQuestionsWithGemini(Context context, String documentContent, int questionCount, String docTitle, AiCallback callback) {
        String apiKey = getApiKey(context);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Nếu chưa nhập API Key -> Tạo danh sách mẫu AI gợi ý thông minh
            if (apiKey == null || apiKey.trim().isEmpty()) {
                mainHandler.post(() -> callback.onSuccess(taoCauHoiAiFallback(docTitle, questionCount)));
                return;
            }

            try {
                // Cấu hình Prompt yêu cầu AI tạo JSON chuẩn
                String promptText = "Bạn là chuyên gia tạo câu hỏi trắc nghiệm. Dựa vào nội dung tài liệu '" + docTitle + "', hãy tạo đúng "
                        + questionCount + " câu hỏi trắc nghiệm tiếng Việt dưới dạng chuỗi JSON thuần túy (không chứa markdown ```json hay giải thích thêm) theo định dạng danh sách mảng JSON:\n"
                        + "[\n"
                        + "  {\n"
                        + "    \"noiDungCauHoi\": \"Tiêu đề câu hỏi...\",\n"
                        + "    \"dapAnDung\": \"Đáp án đúng\",\n"
                        + "    \"dapAnSai1\": \"Đáp án sai 1\",\n"
                        + "    \"dapAnSai2\": \"Đáp án sai 2\",\n"
                        + "    \"dapAnSai3\": \"Đáp án sai 3\"\n"
                        + "  }\n"
                        + "]\n\n"
                        + "Nội dung tài liệu:\n" + (documentContent.length() > 2000 ? documentContent.substring(0, 2000) : documentContent);

                JSONObject contentPart = new JSONObject();
                contentPart.put("text", promptText);

                JSONArray partsArray = new JSONArray();
                partsArray.put(contentPart);

                JSONObject contentsObj = new JSONObject();
                contentsObj.put("parts", partsArray);

                JSONArray contentsArray = new JSONArray();
                contentsArray.put(contentsObj);

                JSONObject rootJson = new JSONObject();
                rootJson.put("contents", contentsArray);

                // Ép Gemini trả JSON thuần, tránh lẫn markdown/giải thích
                JSONObject generationConfig = new JSONObject();
                generationConfig.put("responseMimeType", "application/json");
                rootJson.put("generationConfig", generationConfig);

                byte[] body = rootJson.toString().getBytes(StandardCharsets.UTF_8);

                String lastError = "Gemini API không phản hồi";
                for (String model : GEMINI_MODELS) {
                    HttpURLConnection conn = guiYeuCau(model, apiKey, body);
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                            String line;
                            while ((line = br.readLine()) != null) response.append(line);
                        }

                        List<CauHoi> extractedQuestions = parseGeminiResponse(response.toString());
                        if (extractedQuestions.isEmpty()) {
                            mainHandler.post(() -> callback.onError("AI trả về dữ liệu không đúng định dạng. Vui lòng thử lại."));
                        } else {
                            Log.i(TAG, "Tạo câu hỏi thành công bằng model " + model);
                            mainHandler.post(() -> callback.onSuccess(extractedQuestions));
                        }
                        return;
                    }

                    lastError = docThongBaoLoi(conn, responseCode);
                    Log.w(TAG, "Model " + model + " lỗi " + responseCode + ": " + lastError);

                    // Chỉ chuyển model khi lỗi tạm thời (quá tải / hết hạn mức / lỗi server)
                    boolean loiTamThoi = responseCode == 429 || responseCode == 500 || responseCode == 503;
                    if (!loiTamThoi) break;
                    Thread.sleep(1000);
                }

                String finalError = lastError;
                mainHandler.post(() -> callback.onError(finalError));

            } catch (Exception e) {
                Log.e(TAG, "Gemini API exception", e);
                mainHandler.post(() -> callback.onError("Không kết nối được tới Gemini: " + e.getMessage()));
            }
        });
    }

    private static HttpURLConnection guiYeuCau(String model, String apiKey, byte[] body) throws Exception {
        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(60000);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body);
        }
        return conn;
    }

    private static String docThongBaoLoi(HttpURLConnection conn, int responseCode) {
        String googleMessage = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            googleMessage = new JSONObject(sb.toString()).getJSONObject("error").optString("message", "");
        } catch (Exception ignored) {
        }

        String prefix;
        switch (responseCode) {
            case 400: prefix = "API key không hợp lệ hoặc yêu cầu sai"; break;
            case 403: prefix = "Project của API key bị Google từ chối quyền truy cập"; break;
            case 404: prefix = "Model AI không tồn tại hoặc đã ngừng hỗ trợ"; break;
            case 429: prefix = "Đã vượt hạn mức miễn phí, vui lòng thử lại sau"; break;
            case 503: prefix = "Máy chủ Gemini đang quá tải, vui lòng thử lại sau ít phút"; break;
            default:  prefix = "Gemini API lỗi " + responseCode; break;
        }
        return googleMessage.isEmpty() ? prefix : prefix + " (" + googleMessage + ")";
    }

    private static List<CauHoi> parseGeminiResponse(String jsonResponseStr) {
        List<CauHoi> result = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponseStr);
            JSONArray candidates = root.optJSONArray("candidates");
            if (candidates != null && candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.optJSONObject("content");
                if (content != null) {
                    JSONArray parts = content.optJSONArray("parts");
                    if (parts != null && parts.length() > 0) {
                        String rawText = parts.getJSONObject(0).optString("text", "");

                        // Làm sạch Markdown ```json nếu Gemini tự thêm
                        rawText = rawText.replace("```json", "").replace("```", "").trim();

                        JSONArray jsonArray = new JSONArray(rawText);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String qText = obj.optString("noiDungCauHoi", "Câu hỏi " + (i + 1));
                            String dung = obj.optString("dapAnDung", "Đáp án đúng");
                            String sai1 = obj.optString("dapAnSai1", "Phương án sai 1");
                            String sai2 = obj.optString("dapAnSai2", "Phương án sai 2");
                            String sai3 = obj.optString("dapAnSai3", "Phương án sai 3");

                            result.add(new CauHoi(0, qText, dung, sai1, sai2, sai3));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Không parse được JSON từ Gemini", e);
        }
        return result;
    }

    private static List<CauHoi> taoCauHoiAiFallback(String docTitle, int count) {
        List<CauHoi> resultList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String qText = "Câu " + (i + 1) + " (AI Trích xuất): Kiến thức trọng tâm từ tài liệu " + docTitle + " (Mục " + (i + 1) + ")";
            String dung = "Phương án chính xác theo tài liệu";
            String sai1 = "Lựa chọn sai A";
            String sai2 = "Lựa chọn sai B";
            String sai3 = "Lựa chọn sai C";

            resultList.add(new CauHoi(0, qText, dung, sai1, sai2, sai3));
        }
        return resultList;
    }
}
