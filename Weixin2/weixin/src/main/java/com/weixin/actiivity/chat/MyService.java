package com.weixin.actiivity.chat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.weixin.MyApplication;
import com.weixin.R;
import com.weixin.bean.ImBean;
import com.weixin.tcp.CusEvent;
import com.weixin.tcp.CusEventListener;
import com.weixin.tcp.DataTypeFormat;
import com.weixin.tcp.ProducerTool;
import com.weixin.tcp.TCPCli;
import com.weixin.tcp.myinterface;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr.ZCM on 2016/7/21.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class MyService extends Service implements myinterface,DataTypeFormat {
    private NotificationManager notificationManager;
    private ProducerTool pro;
    private CusEventListener cel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       //连接服务器并一直接收数据
        login();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     * @param message 接收到的消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String message){
        Notification.Builder builder = new Notification.Builder(this);
        //  builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("Dispatch");
        builder.setContentText(message);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
    //测试  25988f39e3354b64b39fa1fb1e2dfcd1     45f679c1eeca465a86ed1a0d415af8cd
    //e505be6118f0450897790b9b6a6d2de8       6bc428bfc043406ab673c42b41913a0b
    public void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //  final String userId = "e505be6118f0450897790b9b6a6d2de8";
                    pro = ProducerTool.getInstance();
                    //  pro.initialize("172.16.6.33", 15656);//"172.16.6.33,"172.16.7.16", 15656
                    pro.initialize("172.16.6.33", 15656);
                    pro.produceMessage(TCPCli.UserLogin(MyApplication.myId));
                    heartBeat();
                    //注册监听器
                    cel =  new CusEventListener(){
                        @Override
                        public void fireCusEvent(CusEvent e) {
                            super.fireCusEvent(e);
                        }
                    };
                    cel.setContext(MyService.this);
                    pro.addCusListener(cel);
                    pro.consumeMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public static  void heartBeat()
    {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        try {
                            Thread.sleep(10000);
                            ProducerTool pro = ProducerTool.getInstance();
                            byte[] ss = new byte[20];
                            pro.produceMessage(ss);
                        } catch (InterruptedException e) {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void getMessage(ImBean bean) {
        String sendId = new String(bean.getSendId());
        Log.d("random",""+sendId);
    }

    @Override
    public void getVoice(byte[] b) throws IOException {

    }

    @Override
    public void getVideo(byte[] b) {

    }

    @Override
    public void getTalk(byte[] b) {

    }

    @Override
    public void onLine() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void getImageContent(byte[] img, int width, int height) {

    }
}
