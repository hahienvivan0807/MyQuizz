package com.example.myquizz.model;

public class BoDe {
    private int id;           // Khóa chính ID bộ đề
    private int thuMucId;     // Khóa ngoại liên kết tới thu_muc(ma_thu_muc)
    private String tieuDe;    // Tiêu đề bộ đề
    private int soLuongCau;   // Số lượng câu hỏi trong bộ đề

    public BoDe() {}

    // Constructor dùng khi đọc từ CSDL (đã có id)
    public BoDe(int id, int thuMucId, String tieuDe, int soLuongCau) {
        this.id = id;
        this.thuMucId = thuMucId;
        this.tieuDe = tieuDe;
        this.soLuongCau = soLuongCau;
    }

    // Constructor dùng khi thêm mới (chưa có id)
    public BoDe(int thuMucId, String tieuDe, int soLuongCau) {
        this.thuMucId = thuMucId;
        this.tieuDe = tieuDe;
        this.soLuongCau = soLuongCau;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getThuMucId() { return thuMucId; }
    public void setThuMucId(int thuMucId) { this.thuMucId = thuMucId; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public int getSoLuongCau() { return soLuongCau; }
    public void setSoLuongCau(int soLuongCau) { this.soLuongCau = soLuongCau; }
}
