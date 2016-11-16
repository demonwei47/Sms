package com.sixin.sms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduActivity extends AppCompatActivity {

    private TextView tv;
    private LocationClient locationClient = null;
    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                tv.setText(bdLocation.getLocType() + bdLocation.getAddrStr() + " " + bdLocation.getBuildingName()
                        + " " + bdLocation.getDistrict() + " " + bdLocation.getFloor() + " " + bdLocation.getStreet()
                        + " " + bdLocation.getTime()+" "+bdLocation.getLatitude()+" "+bdLocation.getLongitude()
                );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu);

        tv = (TextView) findViewById(R.id.tv);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(bdLocationListener);

        LocationClientOption clientOption = new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setIsNeedAddress(true);
        clientOption.setIsNeedLocationDescribe(true);
        clientOption.setIsNeedLocationPoiList(true);

        locationClient.setLocOption(clientOption);
    }


    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                locationClient.start();
                break;

            case R.id.btn2:
                locationClient.stop();
                break;
        }
    }
}
