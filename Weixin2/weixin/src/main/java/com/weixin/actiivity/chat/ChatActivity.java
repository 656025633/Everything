package com.weixin.actiivity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.weixin.MyApplication;
import com.weixin.R;
import com.weixin.actiivity.BaseActivity;
import com.weixin.adapter.ChatAdapter;
import com.weixin.bean.MessageBean;
import com.weixin.tcp.ProducerTool;
import com.weixin.tcp.TCPCli;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatActivity extends BaseActivity implements ChatContract.View {

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
    private ArrayList<MessageBean> datas;
    private boolean isSaying = false;
    private String chatId;
    private ProducerTool pro = ProducerTool.getInstance();
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
//guige
                    List<String> lists = new ArrayList<String>();
                    lists.add(chatId);
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

                    }
                    isSaying = true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mChatPressToSay.setText("按住说话");
                    //将录音发送出去并发送给服务器
                    if (isSaying) {

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
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        adapter = new ChatAdapter(this, datas);
        lv.setAdapter(new ChatAdapter(this, datas));
        //初始化adapter
        receive("bitch");
    }

    @Override
    public void showChatContent(List<MessageBean> messages) {
        datas.clear();
        datas.addAll(messages);
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
    }


    public void receive(String msg) {
        for (int i = 0; i < 20; i++) {
            if (i / 2 == 1) {
                MessageBean bean = new MessageBean();
                bean = new MessageBean();
                bean.setContent(msg);
                bean.setContentType("2");
                bean.setSenderID("zcm");
                bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
                bean.setSenderNick("shdf");
                datas.add(bean);
            } else {
                MessageBean bean = new MessageBean();
                bean = new MessageBean();
                bean.setContent(msg);
                bean.setContentType("2");
                bean.setSenderID("zcmaa");
                bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
                bean.setSenderNick("aimer");
                datas.add(bean);
            }
        }
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
        //清空edittext
        chatEt.setText("");

    }

    interface ReceiveMessage {
        abstract void receive(String msg);

    }
}
