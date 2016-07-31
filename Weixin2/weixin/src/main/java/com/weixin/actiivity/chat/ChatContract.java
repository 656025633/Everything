package com.weixin.actiivity.chat;


import com.weixin.actiivity.BasePresenter;
import com.weixin.actiivity.BaseView;
import com.weixin.bean.MessageBean;

import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ChatContract {
    interface View extends BaseView {
        void showChatContent(List<MessageBean> messages);

    }
    interface Presenter extends BasePresenter<View> {

    }

}
