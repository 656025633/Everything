package com.weixin.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhao on 2016/7/25 0025.
 */
public class ProducerTool {


    private static ProducerTool instance = new ProducerTool();
    Socket socket = null;


    byte[] getUserMessage;

    private Set<CusEventListener> listener= new HashSet<CusEventListener>();


    private ProducerTool() {


    }

    //给事件源注册监听器
    public void addCusListener(CusEventListener cel){
        this.listener.add(cel);
    }

    //当事件发生时,通知注册在该事件源上的所有监听器做出相应的反应（调用回调方法）
    protected void notifies(){
        CusEventListener cel = null;
        Iterator<CusEventListener> iterator = this.listener.iterator();
        while(iterator.hasNext()){
            cel = iterator.next();
            cel.fireCusEvent(new CusEvent(this));
        }
    }

    public static ProducerTool getInstance() {

        return instance;
    }

    // 初始化
    public void initialize(String ip, int port) throws Exception {

        socket = new Socket(ip, port);
        socket.setKeepAlive(true);
        socket.setSoTimeout(10000 * 15*100);
      /*  socket.setSendBufferSize(1024*64);
        socket.setReceiveBufferSize(1024*64);*/
    }

    // 发送消息
    public void produceMessage(byte[] message) throws Exception {

        OutputStream out = socket.getOutputStream();
        out.write(message);
        out.flush();
    }


    public void consumeMessage() throws Exception {

        InputStream in = socket.getInputStream();
        byte[] h1 = new byte[1];
        int offsize = 0;
        while (true) {
            offsize = in.read(h1);
            if (h1[0] != ByteObjConverter.intToByteArray1(0XFFFFFFFF)[0]) {
                continue;
            }
            byte[] h3 = new byte[3];
            offsize = in.read(h3);
            boolean isTrue = true;
            for (int i = 0; i < h3.length; i++) {
                if (h3[i] != ByteObjConverter.intToByteArray1(0XFFFFFFFF)[i + 1]) {
                    isTrue = false;
                    break;
                }
            }
            if (isTrue == false) {
                continue;
            }
            byte[] Length = new byte[4];
            in.read(Length);
            int length = ByteObjConverter.byteArrayToInt(Length);


            byte[] bytes = new byte[length - 2];
            in.read(bytes);

            //检验CRC是否正确
            short crc16_server = CRC16.CRC16(bytes);

            byte[] crcbytes = new byte[2];
            in.read(crcbytes);

            short crc16_client = ByteObjConverter.getShort(crcbytes);

            if (crc16_server != crc16_client) {
                continue;
            }
            if (bytes != null && bytes.length > 0 && bytes.length == (length - 2)) {

                onMessage(bytes.clone());
            }

        }

    }


    // 消息处理函数
    public void onMessage(byte[] message) {
        //触发事件
        this.setGetUserMessage(message);
    }



    public byte[] getGetUserMessage() {
        return getUserMessage;
    }

    public void setGetUserMessage(byte[] getUserMessage) {
        this.getUserMessage = getUserMessage;
        notifies();
    }

    // 关闭连接
    public void close() {

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
