package com.weixin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weixin.R;
import com.weixin.actiivity.chat.ChatActivity;
import com.weixin.adapter.CommonAdapter;
import com.weixin.adapter.ViewHolder;
import com.weixin.view.ActionSlideExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class MessageFragment extends Fragment {
    @BindView(R.id.tv_nickname)
    TextView tv_nickName;

    private List<String> contracts;
    private CommonAdapter contractAdapter;
    private View view;
    ActionSlideExpandableListView list;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_weixin_list,null);
        list = (ActionSlideExpandableListView) view.findViewById(R.id.list);
        init();
        return view;
    }

    /**
     *
     */
    public void initData(){
        contracts = new ArrayList<>();
        for (int i = 0; i <5; i++) {
            contracts.add("e505be6118f0450897790b9b6a6d2de8");//
        }
        contractAdapter = new CommonAdapter<String>(
                getActivity(),
                R.layout.weixin_item,
                contracts) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_name,item);
            }
        };
        list.setDivider(null);
        list.setAdapter(contractAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                String chatId = contracts.get(position);
                intent.putExtra("chatId",chatId);
                startActivity(intent);
            }
        });
    }
    protected void init() {
        contracts = new ArrayList<>();
        for (int i = 0; i <30; i++) {
            contracts.add("6bc428bfc043406ab673c42b41913a0b");
        }
        contractAdapter = new CommonAdapter<String>(
                getActivity(),
                R.layout.expandable_list_item,
                contracts) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.tv_nickname,item.substring(0,5));
            }
        };
        list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

            @Override
            public void onClick(View listView, View buttonview, int position) {
                String actionName = "";
            if(buttonview.getId()==R.id.buttonA) {
                actionName = "对讲";
            } else if(buttonview.getId() == R.id.buttonB){
                actionName = "消息";
                //跳转到聊天界面
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                String chatId = contracts.get(position);
                intent.putExtra("chatId",chatId);
                startActivity(intent);
            }else if(buttonview.getId() == R.id.buttonC){
                actionName = "呼叫";
            }
            else{
                actionName = "命令";
            }
            }
        }, R.id.buttonA, R.id.buttonB,R.id.buttonC,R.id.buttonD);

        list.setAdapter(contractAdapter);
    }

}
