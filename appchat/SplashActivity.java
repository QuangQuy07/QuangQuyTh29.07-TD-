package com.example.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appchat.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chú ý: Sử dụng đúng file layout activity_spalash.xml (có chữ a thừa)
        setContentView(R.layout.activity_spalash);
          new Handler().postDelayed(new Runnable() {
             @Override
              public void run(){
                 if(FirebaseUtil.isloggedIn()){
                     startActivity(new Intent(SplashActivity.this, MainActivity.class));
                 }else {
                     startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                 }
                   finish();
             }
    },1000);
}
}