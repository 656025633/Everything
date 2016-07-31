package com.weixin.adapter;


import com.weixin.R;
import com.weixin.adapter.base.ItemViewDelegate;
import com.weixin.bean.MessageBean;



/**
 * Created by zhy on 16/6/22.
 */
public class MsgComingItemDelagate implements ItemViewDelegate<MessageBean>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.main_chat_from_msg;
    }

    @Override
    public boolean isForViewType(MessageBean item, int position)
    {

        return !"zcm".equals(item.getSenderID());
    }

    @Override
    public void convert(ViewHolder holder, MessageBean chatMessage, int position)
    {
        holder.setText(R.id.chat_from_content, chatMessage.getContent());
    }
}
