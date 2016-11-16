package com.sixin.sms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

public class LocationActivity extends AppCompatActivity {

    private TextView tv;
    public AMapLocationClient LocationClient = null;
    public AMapLocationListener LocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    tv.setText(aMapLocation.toString());
                }
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tv = (TextView) findViewById(R.id.tv);
        LocationClient = new AMapLocationClient(getApplicationContext());
        LocationClient.setLocationListener(LocationListener);


    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                LocationClient.startLocation();
                break;

            case R.id.btn2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationClient.stopLocation();
                    }
                }).start();

                break;
        }
    }
}
