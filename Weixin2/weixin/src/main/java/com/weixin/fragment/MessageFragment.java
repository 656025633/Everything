package com.weixin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.weixin.R;
import com.weixin.actiivity.chat.ChatActivity;
import com.weixin.adapter.CommonAdapter;
import com.weixin.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MessageFragment extends Fragment {

    private List<String> contracts;
    private CommonAdapter contractAdapter;
    private View view;
    ListView list;
    private CommonAdapter mAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_weixin_list,null);
        list = (ListView) view.findViewById(R.id.list);
        initData();
        return view;
    }

    /**
     *
     */
    public void initData(){
        contracts = new ArrayList<>();
        for (int i = 0; i <1 ; i++) {
            contracts.add("6bc428bfc043406ab673c42b41913a0b"+i);
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
}
