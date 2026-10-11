package com.example.myquizz.model;

public class ThuMuc {
    private int maThuMuc;       // ID thư mục (khóa chính)
    private int userId;         // ID người dùng sở hữu thư mục này (khóa ngoại)
    private String tenThuMuc;   // Tên thư mục (ví dụ: "Tiếng Anh")
    private String moTaThuMuc;  // Mô tả (ví dụ: "8 tài liệu")

    public ThuMuc() {}

    // Constructor khi đọc từ CSDL (đã có maThuMuc)
    public ThuMuc(int maThuMuc, int userId, String tenThuMuc, String moTaThuMuc) {
        this.maThuMuc = maThuMuc;
        this.userId = userId;
        this.tenThuMuc = tenThuMuc;
        this.moTaThuMuc = moTaThuMuc;
    }

    // Constructor khi thêm mới vào CSDL (chưa có maThuMuc)
    public ThuMuc(String tenThuMuc, int userId, String moTaThuMuc) {
        this.tenThuMuc = tenThuMuc;
        this.userId = userId;
        this.moTaThuMuc = moTaThuMuc;
    }

    public int getMaThuMuc() { return maThuMuc; }
    public void setMaThuMuc(int maThuMuc) { this.maThuMuc = maThuMuc; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTenThuMuc() { return tenThuMuc; }
    public void setTenThuMuc(String tenThuMuc) { this.tenThuMuc = tenThuMuc; }

    public String getMoTaThuMuc() { return moTaThuMuc; }
    public void setMoTaThuMuc(String moTaThuMuc) { this.moTaThuMuc = moTaThuMuc; }
}
