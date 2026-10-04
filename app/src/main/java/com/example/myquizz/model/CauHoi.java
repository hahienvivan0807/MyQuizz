package com.example.myquizz.model;

public class CauHoi {
    private int id;                 // Khóa chính ID câu hỏi
    private int boDeId;             // Khóa ngoại liên kết tới bo_de(id)
    private String noiDungCauHoi;   // Nội dung câu hỏi
    private String dapAnDung;       // 1 đáp án đúng
    private String dapAnSai1;       // Đáp án sai 1
    private String dapAnSai2;       // Đáp án sai 2
    private String dapAnSai3;       // Đáp án sai 3

    public CauHoi() {}

    // Constructor khi đọc từ DB (đã có id)
    public CauHoi(int id, int boDeId, String noiDungCauHoi, String dapAnDung, String dapAnSai1, String dapAnSai2, String dapAnSai3) {
        this.id = id;
        this.boDeId = boDeId;
        this.noiDungCauHoi = noiDungCauHoi;
        this.dapAnDung = dapAnDung;
        this.dapAnSai1 = dapAnSai1;
        this.dapAnSai2 = dapAnSai2;
        this.dapAnSai3 = dapAnSai3;
    }

    // Constructor khi thêm mới (chưa có id)
    public CauHoi(int boDeId, String noiDungCauHoi, String dapAnDung, String dapAnSai1, String dapAnSai2, String dapAnSai3) {
        this.boDeId = boDeId;
        this.noiDungCauHoi = noiDungCauHoi;
        this.dapAnDung = dapAnDung;
        this.dapAnSai1 = dapAnSai1;
        this.dapAnSai2 = dapAnSai2;
        this.dapAnSai3 = dapAnSai3;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBoDeId() { return boDeId; }
    public void setBoDeId(int boDeId) { this.boDeId = boDeId; }

    public String getNoiDungCauHoi() { return noiDungCauHoi; }
    public void setNoiDungCauHoi(String noiDungCauHoi) { this.noiDungCauHoi = noiDungCauHoi; }

    public String getDapAnDung() { return dapAnDung; }
    public void setDapAnDung(String dapAnDung) { this.dapAnDung = dapAnDung; }

    public String getDapAnSai1() { return dapAnSai1; }
    public void setDapAnSai1(String dapAnSai1) { this.dapAnSai1 = dapAnSai1; }

    public String getDapAnSai2() { return dapAnSai2; }
    public void setDapAnSai2(String dapAnSai2) { this.dapAnSai2 = dapAnSai2; }

    public String getDapAnSai3() { return dapAnSai3; }
    public void setDapAnSai3(String dapAnSai3) { this.dapAnSai3 = dapAnSai3; }
}
