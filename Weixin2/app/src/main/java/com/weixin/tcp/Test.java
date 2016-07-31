package com.weixin.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhao on 2016/7/25 0025.
 */


public class Test {
    public static void main(String[] args) {

        try {
            String userId = "e505be6118f0450897790b9b6a6d2de8";
            ProducerTool pro = ProducerTool.getInstance();
            //初始化连接
            pro.initialize("172.16.6.33", 15656);
            //登录
            pro.produceMessage(TCPCli.UserLogin(userId));
            //注册监听器
            pro.addCusListener(new CusEventListener() {
                @Override
                public void fireCusEvent(CusEvent e) {
                    super.fireCusEvent(e);
                }
            });

            //心跳保持
            heartBeat();

            //监听接收数据
            pro.consumeMessage();

            //退出
            // pro.produceMessage(TCPCli.UserLoginOut("e505be6118f0450897790b9b6a6d2de8"));


        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
