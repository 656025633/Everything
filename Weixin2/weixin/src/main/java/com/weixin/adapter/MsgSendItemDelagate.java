package com.weixin.adapter;


import com.weixin.R;
import com.weixin.adapter.base.ItemViewDelegate;
import com.weixin.bean.MessageBean;



/**
 * Created by zhy on 16/6/22.
 */
public class MsgSendItemDelagate implements ItemViewDelegate<MessageBean>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.main_chat_send_msg;
    }

    @Override
    public boolean isForViewType(MessageBean item, int position)
    {
        //此处判断返回的布局
        return "zcm".equals(item.getSenderID());
    }

    @Override
    public void convert(ViewHolder holder, MessageBean chatMessage, int position)
    {
        holder.setText(R.id.chat_send_content, chatMessage.getContent());
        holder.setText(R.id.chat_send_name,chatMessage.getSenderID());
    }
}
