package com.example.myquizz.model;

public class User {
    private int id;          // ID người dùng (khóa chính trong CSDL)
    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
    private String email;    // Email

    public User() {}

    // Constructor khi đọc từ CSDL (đã có id)
    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Constructor khi đăng ký / thêm mới (chưa có id)
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
