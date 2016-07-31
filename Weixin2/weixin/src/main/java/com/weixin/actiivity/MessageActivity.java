package com.weixin.actiivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.weixin.R;
import com.weixin.actiivity.chat.MyService;
import com.weixin.fragment.MessageFragment;

public class MessageActivity extends AppCompatActivity {
    MessageFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        startService();
    }

    /**
     * 启动服务
     */
    private void startService() {
        Intent intent = new Intent(MessageActivity.this, MyService.class);
        startActivity(intent);
    }

    private void initView() {
        fragment = new MessageFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content,fragment).commit();

    }
}
