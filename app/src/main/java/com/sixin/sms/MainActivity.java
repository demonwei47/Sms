package com.sixin.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "yzm":
                    editText.setText(intent.getExtras().getString("code"));
                    break;
            }
        }
    };
    private EditText editText;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, SmsService.class);

        editText = (EditText) findViewById(R.id.et);

        registerReceiver(receiver, new IntentFilter("yzm"));
        getWindow().addFlags(WindowManager.LayoutParams. FLAG_SECURE);
    }


    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                intent.putExtra("cmd", 1);
                startService(intent);
                break;

            case R.id.btn2:
//                intent.putExtra("cmd", 0);
//                startService(intent);
                stopService(intent);
                break;

            case R.id.btn3:
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                break;

            case R.id.btn4:
                startActivity(new Intent(MainActivity.this, BaiduActivity.class));
                break;


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
