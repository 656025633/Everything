package com.weixin.actiivity.chat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weixin.MyApplication;
import com.weixin.R;
import com.weixin.actiivity.BaseActivity;
import com.weixin.adapter.ChatAdapter;
import com.weixin.bean.ImBean;
import com.weixin.bean.MessageBean;
import com.weixin.tcp.DataType;
import com.weixin.tcp.DataTypeFormat;
import com.weixin.tcp.Media;
import com.weixin.tcp.ProducerTool;
import com.weixin.tcp.TCPCli;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class ChatActivity extends BaseActivity implements ChatContract.View,DataTypeFormat {

    @BindView(R.id.chat_et)
    EditText chatEt;
    @BindView(R.id.chat_send)
    Button mChatSend;
    @BindView(R.id.chat_pressToSay)
    Button mChatPressToSay;
    @BindView(R.id.chat_ll_voice)
    LinearLayout mChatLlVoice;
    @BindView(R.id.chat_changeToText)
    ImageView mChatChangeToText;
    @BindView(R.id.chat_text)
    LinearLayout mChatText;
    private ChatPresenter mChatPresenter;
    private static ChatAdapter adapter;
    @BindView(R.id.chat_changeToVoice)
    ImageView mChatVoice;
    @BindView(R.id.listview)
    ListView lv;
    @BindView(R.id.chat_all_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.chatUser)
    TextView chatUser;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_choosePic)
    ImageView iv_choosePic;
    private ArrayList<ImBean> datas;
    private boolean isSaying = false;
    private String chatId;
    private ProducerTool pro = ProducerTool.getInstance();
    Media mMedia = new Media();
    private Bitmap mBm;


    //定义我的主机地址

    @Override
    protected void beforeView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_chat;
    }

    @Override
    protected void obtainIntent() {
        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if(!TextUtils.isEmpty(chatId)){
            chatUser.setText(chatId.substring(0,5));
        }
    }

    @Override
    protected void initListener() {
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    imm.hideSoftInputFromWindow(chatEt.getWindowToken(), 0);
                }
                return false;
            }
        });

        chatEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setSelection(adapter.getCount() - 1);
            }
        });


        mChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(chatEt.getText().toString().trim())) {
                    //发送出去
                 /*   MessageBean bean = new MessageBean();
                    bean = new MessageBean();
                    bean.setContent(chatEt.getText().toString().trim());
                    bean.setContentType("2");
                    //获取自己的userid
                    String ownid = "zcm";
                    bean.setSenderID(ownid);
                    bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
                    bean.setSenderNick("shdf");*/
//
                    List<String> lists = new ArrayList<String>();
                    lists.add(chatId);
                    Log.d("random",""+chatId);
                    lists.add(MyApplication.myId);
                    try {
                        pro.produceMessage(TCPCli.UserIMWord(MyApplication.myId, lists,chatEt.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                //    datas.add(bean);
                  /*  adapter.notifyDataSetChanged();
                    lv.setSelection(adapter.getCount() - 1);*/
                    //清空edittext
                    chatEt.setText("");
                }
                if (!TextUtils.isEmpty(chatEt.getText().toString().trim())) {

                }
            }
        });
        //点击录音按钮，切换到录音界面

        mChatVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatText.setVisibility(View.GONE);
                //键盘收回
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(chatEt.getWindowToken(), 0);
                mChatLlVoice.setVisibility(View.VISIBLE);
            }
        });
        //切换到发送文字的界面

        mChatChangeToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatLlVoice.setVisibility(View.GONE);
                mChatText.setVisibility(View.VISIBLE);
            }
        });

        mChatPressToSay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下的时候
                    mChatPressToSay.setText("正在说话。。。");
                    //启动录音
                    if (!isSaying) {
                        mMedia.startRecord();
                    }
                    isSaying = true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mChatPressToSay.setText("按住说话");
                    //将录音发送出去并发送给服务器
                    if (isSaying) {
                        mMedia.stopRecord();
                        //发送出去
                        sendAudio();
                    }
                    isSaying = false;
                }
                return true;
            }
        });
        mChatPressToSay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //打开本地相册
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        adapter = new ChatAdapter(this, datas);
        lv.setAdapter(new ChatAdapter(this, datas));
        //初始化adapter
        ChatPresenter presenter = new ChatPresenter();
        presenter.attachView(this);
        presenter.loadData("",this);
      //  receive("bitch");
    }


    public void showChatContent(List<ImBean> messages) {
        datas.clear();
        datas.addAll(messages);
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
    }


    //接受服务发送过来的消息
    public void onEventMainThread(ImBean bean) {
        datas.add(bean);
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
    }

    interface ReceiveMessage {
        abstract void receive(String msg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //发送音频
    public void sendAudio(){
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                try {
                    List<String> lists = new ArrayList<String>();
                    lists.add(chatId);//接收者
                    lists.add(MyApplication.myId);
                    byte content[] = mMedia.getByteArrayFrom("");
                    //将音频文件读取为字节数组
                    byte[] bytes =  TCPCli.UserIMVoice(MyApplication.myId,lists, DataType.UP_Chat_Voice,content);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                byte[] imgContent = Bitmap2Bytes(bitmap);
              /*  sendPic(imgContent, bitmap.getHeight(), bitmap.getWidth());*/
               /* ImageView imageView = (ImageView) findViewById(R.id.iv_back);
                *//* 将Bitmap设定到ImageView *//*
                imageView.setImageBitmap(bitmap);*/
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void sendPic(final byte[] imageContent, final int imgHeight, final int imgWidth) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> list = new ArrayList<String>();
                    list.add(MyApplication.myId);
                    list.add(chatId);
                    pro.produceMessage(TCPCli.UserIMImages(MyApplication.myId, list, PNG, imgHeight, imgWidth, imageContent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public byte[] Bitmap2Bytes(Bitmap bm) {
        mBm = bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 10, baos);
        return baos.toByteArray();
    }

}
