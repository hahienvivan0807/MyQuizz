package com.example.myquizz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquizz.R;
import com.example.myquizz.activity.LamBaiActivity;
import com.example.myquizz.model.BoDe;

import java.util.List;

/**
 * ADAPTER HIỂN THỊ DANH SÁCH BỘ ĐỀ
 * Vai trò: Nạp danh sách các Bộ Đề (List<BoDe>) lên RecyclerView
 */
public class BoDeAdapter extends RecyclerView.Adapter<BoDeAdapter.BoDeViewHolder> {

    // Context màn hình đang sử dụng Adapter này
    private Context context;

    // Danh sách dữ liệu các Bộ Đề (đúng kiểu dữ liệu BoDe, không phải VatPham)
    private List<BoDe> danhSachBoDe;

    // Hàm khởi tạo Adapter: nhận context và danh sách BoDe từ Activity truyền vào
    public BoDeAdapter(Context context, List<BoDe> danhSachBoDe) {
        this.context = context;
        this.danhSachBoDe = danhSachBoDe;
    }

    // Hàm cập nhật danh sách mới khi có thay đổi dữ liệu (thêm/sửa/xóa bộ đề)
    public void capNhatBoDe(List<BoDe> danhSachMoi) {
        this.danhSachBoDe = danhSachMoi;
        notifyDataSetChanged(); // Phát thông báo yêu cầu RecyclerView vẽ lại giao diện
    }

    // Nạp file giao diện XML của từng thẻ bộ đề (item_vat_pham) thành đối tượng View
    @NonNull
    @Override
    public BoDeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vat_pham, parent, false);
        return new BoDeViewHolder(view);
    }

    // Gán dữ liệu của từng Bộ Đề tại vị trí 'position' vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull BoDeViewHolder holder, int position) {
        BoDe boDe = danhSachBoDe.get(position);

        // Hiển thị Tiêu đề và Số lượng câu hỏi của bộ đề
        holder.txtTieuDe.setText(boDe.getTieuDe());
        holder.txtMoTa.setText(boDe.getSoLuongCau() + " câu hỏi");

        // Sự kiện khi bấm vào 1 bộ đề -> Chuyển sang màn hình Làm bài
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LamBaiActivity.class);
            intent.putExtra("BO_DE_ID", boDe.getId());
            intent.putExtra("BO_DE_TIEU_DE", boDe.getTieuDe());
            context.startActivity(intent);
        });
    }

    // Trả về tổng số lượng Bộ Đề có trong danh sách
    @Override
    public int getItemCount() {
        return danhSachBoDe != null ? danhSachBoDe.size() : 0;
    }

    // Lớp ViewHolder: Ánh xạ các View trong file XML để lưu trữ (cache) ID
    public static class BoDeViewHolder extends RecyclerView.ViewHolder {
        TextView txtTieuDe, txtMoTa;

        public BoDeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDeVatPham);
            txtMoTa = itemView.findViewById(R.id.txtMoTaVatPham);
        }
    }
}
