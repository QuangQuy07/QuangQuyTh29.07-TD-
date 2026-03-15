package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText editTextPhoneNumber;
    Button getOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        countryCodePicker = findViewById(R.id.login_countrycode);
        editTextPhoneNumber = findViewById(R.id.login_mobile_number);
        getOtpBtn = findViewById(R.id.send_otp_btn); // ID đúng trong activity_login_phone_number.xml
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(editTextPhoneNumber);

        getOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                editTextPhoneNumber.setError("Số điện thoại không hợp lệ");
                return;
            }
            
            // Chuyển sang màn hình nhập OTP
            Intent intent = new Intent(LoginPhoneNumberActivity.this, loginOtpActivity.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}