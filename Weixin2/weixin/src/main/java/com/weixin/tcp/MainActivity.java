package com.weixin.tcp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weixin.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import utils.FileUtil;


public class MainActivity extends AppCompatActivity implements myinterface,DataType,DataTypeFormat{
    @BindView(R.id.login) Button login;
    TextView state,tv_send,tv_receive;
    Button exit;
    Button btn_voice,btn_record,btn_stop_record,btn_talk,btnSendPic,btnSendVideo;
    ImageView ivBack,ivSend;
    EditText et_ip,et_port;
    private Socket mSocket;
    private OutputStream out;
    private InputStream in;
    Media mMedia = new Media();
    boolean talkState = false;
    TalkUitl tu = new TalkUitl();
    String myip="";
    //测试  25988f39e3354b64b39fa1fb1e2dfcd1     45f679c1eeca465a86ed1a0d415af8cd
    //e505be6118f0450897790b9b6a6d2de8       6bc428bfc043406ab673c42b41913a0b
    String userId = "6bc428bfc043406ab673c42b41913a0b";
    //接收者
    String receiveId = "6bc428bfc043406ab673c42b41913a0b";

    //
    byte IM_PIC = 3;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,"连接服务器成功",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    state.setText(myip+"在线");
                    break;
                case 2:
                    tv_send.setText("发送的文字："+msg.obj.toString());
                    break;
                case 3:
                    tv_receive.setText("");
                    //Toast.makeText(MainActivity.this,"消息发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this,"消息接收成功",Toast.LENGTH_SHORT).show();

                    break;
                case  100:
                    tv_receive.setText("接收到的文字："+msg.obj.toString());
                    break;
                case  110:
                    Bitmap bitmap = Bytes2Bimap((byte[]) msg.obj);
                    ivSend.setImageBitmap(bitmap);
                    break;
                case  102:
                    Toast.makeText(MainActivity.this,"已经掉线",Toast.LENGTH_SHORT).show();
                    state.setText("离线");
                    break;
                case 200:
                    ivBack.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
    private ProducerTool pro;
    private Button text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        myip = IPUtil.getIP(this);
    }


    private void initView()  {
        receive();
        login = (Button) this.findViewById(R.id.login);
        text1 = (Button) this.findViewById(R.id.sendText);
        state = (TextView) this.findViewById(R.id.state);
        tv_send  = (TextView) this.findViewById(R.id.tv_send);
        tv_receive = (TextView) this.findViewById(R.id.tv_receive);
        exit  = (Button) this.findViewById(R.id.exit);
        btn_voice = (Button) this.findViewById(R.id.btn_voice);
        btn_record = (Button) this.findViewById(R.id.btn_record);
        btn_stop_record = (Button) this.findViewById(R.id.btn_stop_record);
        btnSendPic = (Button) this.findViewById(R.id.btnSendPic);
        btn_talk = (Button) this.findViewById(R.id.btn_talk);
        btnSendVideo = (Button) this.findViewById(R.id.btnSendVideo);
        ivBack = (ImageView) findViewById(R.id.iv_back_img);
        ivSend = (ImageView) findViewById(R.id.iv_send_img);
        et_ip = (EditText) this.findViewById(R.id.et_ip);
        et_port = (EditText) this.findViewById(R.id.et_port);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送数据给服务器
             login();
            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送文字
                sendText();
            }
        });
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           sendAudio();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pro.produceMessage(TCPCli.UserLoginOut(userId));
                            pro.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                }

        });
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AudioFile.record();
                mMedia.startRecord();
                Log.d("random","开始录音 ");
            }
        });
        btn_stop_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mMedia.stopRecord();
                Log.d("random","停止录音 ");
            }
        });

        btn_talk.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if(MotionEvent.ACTION_DOWN ==event.getAction()){
                   //按下
                   talkState =true;
                   List<String> lists = new ArrayList<String>();
                   //lists.add(userId);
                   lists.add(receiveId);//接收者
                   tu.say(true,   userId, lists, UP_Chat_Voice);

               }
               else  if(MotionEvent.ACTION_UP ==event.getAction()){
                   tu.say(false,  userId, null, UP_Chat_Voice);
               }
               return false;
           }
       });
        btnSendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送图片
                Bitmap bitmap = getImageFromAssetsFile("1.png");
                byte[] imgContent = Bitmap2Bytes(bitmap);
                sendPic(imgContent, bitmap.getHeight(), bitmap.getWidth());
            }
        });
        btnSendVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendVideo();
            }
        });
    }


    //启动线程一直接收数据
    public void receive(){

    }
    CusEventListener cel;
    //登录
    public void login(){
        if(TextUtils.isEmpty(et_port.getText().toString().trim())|TextUtils.isEmpty(et_port.getText().toString().trim())){
            Toast.makeText(this,"先输入服务器地址和端口",Toast.LENGTH_SHORT).show();
            return ;
        }
        final String ip_address = et_ip.getText().toString().trim();
        final int portStr = Integer.parseInt(et_port.getText().toString().trim());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                  //  final String userId = "e505be6118f0450897790b9b6a6d2de8";
                    pro = ProducerTool.getInstance();
                  //  pro.initialize("172.16.6.33", 15656);//"172.16.6.33,"172.16.7.16", 15656
                    pro.initialize(ip_address, portStr);
                    pro.produceMessage(TCPCli.UserLogin(userId));
                    heartBeat();
                    //注册监听器
                    cel =  new CusEventListener(){
                        @Override
                        public void fireCusEvent(CusEvent e) {
                            super.fireCusEvent(e);
                        }
                    };
                    cel.setContext(MainActivity.this);
                    pro.addCusListener(cel);

                /*    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                    singleThreadExecutor.execute(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(5000);
                                for (int i = 0; i <100000 ; i++) {
                                    List<String> lists = new ArrayList<String>();
                                    //lists.add(userId);
                                    lists.add(receiveId);//接收者
                                    //lists.add(receiveId2);//接收者
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("万向融通android-->");
                                    //发送消息
                                    Message msg = new Message();
                                    msg.what = 2;
                                    msg.obj = sb.toString()+i;
                                    mHandler.sendMessage(msg);
                                    pro.produceMessage(TCPCli.UserIMWord(userId, lists, sb.toString()+i));

                                    Thread.sleep(5000);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/

                    // pro.produceMessage(TCPCli.UserLoginOut("e505be6118f0450897790b9b6a6d2de8"));
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

    /**
     *
     */
        public void sendText(){
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                        for (int i = 0; i <100000 ; i++) {
                            List<String> lists = new ArrayList<String>();
                            //lists.add(userId);
                            lists.add(receiveId);//接收者
                            //lists.add(receiveId2);//接收者
                            StringBuilder sb = new StringBuilder();
                            sb.append(myip+"-->");
                            //发送消息
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = sb.toString()+i;
                            mHandler.sendMessage(msg);
                            pro.produceMessage(TCPCli.UserIMWord(userId, lists, sb.toString()+i));

                            Thread.sleep(5000);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    //发送音频
    public void sendAudio(){
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                try {
                    List<String> lists = new ArrayList<String>();
                    //lists.add(userId);
                    lists.add(receiveId);//接收者
                    byte content[] = mMedia.getByteArrayFrom("");
                    //将音频文件读取为字节数组
                    byte[] bytes =  TCPCli.UserIMVoice(userId,lists,DataType.UP_Chat_Voice,content);
                    if(bytes!=null && bytes.length>0) {
                        byte[] sends = bytes.clone();
                        pro.produceMessage(sends);
                        Log.d("random", "voice successful");
                   /* for (int i = 0; i <100000 ; i++) {
                        List<String> lists = new ArrayList<String>();
                        //lists.add(userId);
                        lists.add(receiveId);//接收者
                        //lists.add(receiveId2);//接收者
                       // String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music.mp3";
                        byte content[] = mMedia.getByteArrayFrom("");
                        //将音频文件读取为字节数组
                        pro.produceMessage(TCPCli.UserIMVoice(userId,lists,DataType.UP_Chat_Voice,content));
                        Thread.sleep(2000);
                    }*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送视频
     */
    public void sendVideo(){
        Intent intent  = VideoUtil.recordVideo("");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(100==requestCode&&resultCode==RESULT_OK){
            Uri uri = data.getData();
            final String path = FileUtil.getPath(this,uri);
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        List<String> lists = new ArrayList<String>();
                        //lists.add(userId);
                        lists.add(receiveId);//接收者
                        byte content[] = mMedia.getByteArrayFrom(path);
                        //将音频文件读取为字节数组
                        byte[] bytes =  TCPCli.UserIMVideo(userId,lists,DataType.UP_Chat_Video,content);
                        if(bytes!=null && bytes.length>0) {
                            byte[] sends = bytes.clone();
                            pro.produceMessage(sends);
                            Log.d("random", "video successful");
                   /* for (int i = 0; i <100000 ; i++) {
                        List<String> lists = new ArrayList<String>();
                        //lists.add(userId);
                        lists.add(receiveId);//接收者
                        //lists.add(receiveId2);//接收者
                       // String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music.mp3";
                        byte content[] = mMedia.getByteArrayFrom("");
                        //将音频文件读取为字节数组
                        pro.produceMessage(TCPCli.UserIMVoice(userId,lists,DataType.UP_Chat_Voice,content));
                        Thread.sleep(2000);
                    }*/
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.d("random","path:"+path);
            Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"what are you 弄啥叻",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接
        try {
            if(mSocket!=null){
            mSocket.close();
            in.close();
            out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static byte[] getByteArrayFrom(String path){

        byte[] result=null;

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        //创建文件
        File file=new File(path);
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(file);
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=fileInputStream.read(buffer))!=-1){
                outputStream.write(buffer, 0, len);
            }
            result=outputStream.toByteArray();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                    fileInputStream=null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    //截取字符串
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }

    /**
     * 将图片内容解析成字节数组
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
    /**
     * @param
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
    /**
     * 把字节数组保存为一个文件
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                    mMedia.startPlay(outputFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
    //图片
    /**
     * bitmap转byte[]
     *
     * @param bm
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]转bitmap
     *
     * @param b
     * @return
     */
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 获取assets中图片
     *
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 发送图片
     *
     * @param imageContent
     * @param imgHeight
     * @param imgWidth
     */
    private void sendPic(final byte[] imageContent, final int imgHeight, final int imgWidth) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> list = new ArrayList<String>();
                    list.add(userId);
                    list.add(receiveId);
                    pro.produceMessage(TCPCli.UserIMImages(userId, list, PNG, imgHeight, imgWidth, imageContent));
                    System.out.println("sendPic:>>>>图片已发送" + imageContent.length);
                    //发送消息
                    Message msg = new Message();
                    msg.what = 110;
                    msg.obj = imageContent;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 接受图片
     * @param img
     * @param width
     * @param height
     */
    @Override
    public void getImageContent(byte[] img, int width, int height) {
        System.out.println("width & height & imgLength=" + width + " & " + height + img.length);
        try {
            getFileFromBytes(img, Environment.getExternalStorageDirectory().getCanonicalPath().toString()
                    + "/wxrtpic"+"/test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = Bytes2Bimap(img);
        Message msg = new Message();
        msg.obj = bitmap;
        msg.what = 200;
        mHandler.sendMessage(msg);

    }

    @Override
    public void getMessage(String str) {
        Log.d("random","sss"+str);
        Message msg = new Message();
        msg.obj = str;
        msg.what=100;
        mHandler.sendMessage(msg);
        if(str != null){

        }
    }

    @Override
    public void getVoice(byte b[])  {
        //将语音保存
        String sendpath = null;
        try {
            sendpath = Environment.getExternalStorageDirectory().getCanonicalPath().toString()
                     + "/MessageMediaReceive"+"/zcm.mp3";
            getFileFromBytes(b,sendpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVideo(byte[] b) {
        String sendpath = null;
        try {
            sendpath = Environment.getExternalStorageDirectory().getCanonicalPath().toString()
                    + "/MessageVideoReceive"+"/zcm.mp4";
            getFileFromBytes(b,sendpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTalk(byte[] b) {
       tu.receive(b);

    }

    @Override
    public void onLine() {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void exit() {
        mHandler.sendEmptyMessage(102);

    }
}
