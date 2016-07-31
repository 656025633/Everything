package com.weixin.adapter;

import android.content.Context;

import com.weixin.bean.MessageBean;

import java.util.List;



/**
 * Created by zhy on 15/9/4.
 */
public class ChatAdapter extends MultiItemTypeAdapter<MessageBean>
{
    public ChatAdapter(Context context, List<MessageBean> datas)
    {
        super(context, datas);

        addItemViewDelegate(new MsgSendItemDelagate());
        addItemViewDelegate(new MsgComingItemDelagate());
    }

}
