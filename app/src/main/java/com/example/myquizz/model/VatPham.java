package com.example.myquizz.model;

public class VatPham {
    private int maSo;      // ID của vật phẩm
    private String tieuDe; // Tiêu đề (ví dụ: "Ôn tập Sinh học 10")
    private String moTa;   // Mô tả phụ (ví dụ: "15 câu hỏi · Khoa học")

    public VatPham() {}

    // Dùng khi ĐỌC dữ liệu từ CSDL (đã có maSo)
    public VatPham(int maSo, String tieuDe, String moTa) {
        this.maSo = maSo;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
    }

    // Dùng khi THÊM MỚI vật phẩm vào CSDL (SQLite tự sinh maSo)
    public VatPham(String tieuDe, String moTa) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
    }

    public int getMaSo() { return maSo; }
    public void setMaSo(int maSo) { this.maSo = maSo; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
