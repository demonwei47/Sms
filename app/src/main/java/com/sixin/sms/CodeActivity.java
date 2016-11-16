package com.sixin.sms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        setTitle("code");

        String code=getIntent().getStringExtra("code");
        Toast.makeText(this,"code "+code,Toast.LENGTH_LONG).show();

        TextView tv= (TextView) findViewById(R.id.tv);
        tv.setText(code);

    }
}
