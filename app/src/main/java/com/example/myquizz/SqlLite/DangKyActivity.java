package com.example.myquizz.SqlLite;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myquizz.R;
import com.google.android.material.textfield.TextInputEditText;

public class DangKyActivity extends AppCompatActivity {

    // Khai báo các view từ giao diện activity_register.xml
    private TextInputEditText fullNameInput, emailInput, passwordInput, confirmPasswordInput;
    private CheckBox termsCheckbox;
    private Button registerButton;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kết nối Activity với layout activity_register.xml
        setContentView(R.layout.activity_register);

        AddViews();
        AddEvents();
    }

    private void AddViews() {
        // Ánh xạ các view theo ID trong activity_register.xml
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);
    }

    private void AddEvents() {
        // Viết các sự kiện click ở đây
    }
}
