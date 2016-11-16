package com.sixin.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by demon9 on 2016/11/14 0014.
 */
public class SmsService extends Service {

    private NotificationManager manger;
    public static NotificationCompat.Builder builder;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SmsMessage msg = null;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object p : pdusObj) {
                    msg = SmsMessage.createFromPdu((byte[]) p);
                    String msgTxt = msg.getMessageBody();//得到消息的内容
                    Date date = new Date(msg.getTimestampMillis());//时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String receiveTime = format.format(date);
                    String senderNumber = msg.getOriginatingAddress();
                    //Toast.makeText(context, "发送人：" + senderNumber + "  短信内容：" + msgTxt + "接受时间：" + receiveTime, Toast.LENGTH_LONG).show();


                    //   \d{4,6}(?!\d)
                    // [A-Za-z0-9]{4,}(?![A-Za-z0-9])
                    Pattern pattern=Pattern.compile("[A-Za-z0-9]{4,}(?![A-Za-z0-9])");
                    Matcher matcher=pattern.matcher(msgTxt);
                    if (matcher.find()){
                        String code=matcher.group(0);
                        Toast.makeText(context,"验证码是 "+code,Toast.LENGTH_LONG).show();
                        context.sendBroadcast(new Intent().setAction("yzm").putExtra("code",code));
                        createNotification(code);
                    }
                    return;
                }
                return;
            }
        }
    };

    private void createNotification(String code) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("收到验证码 "+code);

        Intent[] intents = new Intent[2];
        intents[0]=Intent.makeRestartActivityTask(new ComponentName(this,MainActivity.class));
        intents[1]=new Intent(this,CodeActivity.class);
        intents[1].putExtra("code",code);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manger.notify(666, notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("sms listening");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        //manger.notify(333, notification);
        startForeground(333, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = intent.getExtras().getInt("cmd");
        if (1 == i) {
            initReceiver();
        } else {
            stopForeground(true);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        stopForeground(true);
    }
}
