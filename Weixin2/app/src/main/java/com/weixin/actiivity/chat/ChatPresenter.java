package com.weixin.actiivity.chat;

import android.content.Context;

import com.weixin.bean.ImBean;
import com.weixin.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private List<ImBean> datas;
    private Context mContext;


    /**
     * 加载网络数据
     * 根据chatid去得文件的名字
     */
    public void loadData(String filename,Context context){
            ImBean bean;
            datas = new ArrayList<ImBean>();
  /*      try {
            Dao dao = DatabaseHelper.getHelper(context).getDao(ChatStringBean.class);
            ChatStringBean csbean = new ChatStringBean();
            csbean.setiAndWho(filename);
            csbean.setJsonContent("jsonString");
            dao.create(csbean);
            //根据条件查询
            List<ChatStringBean> l = dao.queryBuilder().where().eq("iAndWho",filename).query();
            for (int i = 0; i <l.size() ; i++) {
                Log.d("random",l.get(i).getJsonContent()+"teset");
            }
            ChatStringBean c = new ChatStringBean();
            c.setiAndWho(filename);
            c.setJsonContent("teeeeeeee");

            dao.update(c);
            List<ChatStringBean> ll = dao.queryBuilder().where().eq("iAndWho",filename).query();
            for (int i = 0; i <ll.size() ; i++) {
                Log.d("random",ll.get(i).getJsonContent()+"teset");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }*/
//测试  25988f39e3354b64b39fa1fb1e2dfcd1     45f679c1eeca465a86ed1a0d415af8cd
        //e505be6118f0450897790b9b6a6d2de8       6bc428bfc043406ab673c42b41913a0b
        for (int i = 0; i < 20; i++) {
                    if(i%2==0){
                            bean = new ImBean();
                        bean.setType(0);
                            bean.setContent("我是个大好人呢".getBytes());
                            bean.setSendId("e505be6118f0450897790b9b6a6d2de8".getBytes());
                            datas.add(bean);
                    }
                    else {
                        bean = new ImBean();
                        bean.setType(0);
                        bean.setContent("aa".getBytes());
                        bean.setSendId("6bc428bfc043406ab673c42b41913a0b".getBytes());
                        datas.add(bean);
                    }
            }
        for (int i = 0; i < 5; i++) {
            bean = new ImBean();
            bean.setContent("voice".getBytes());
            bean.setSendId("e505be6118f0450897790b9b6a6d2de8".getBytes());
            bean.setType(1);
            datas.add(bean);
        }
        for (int i = 0; i < 5; i++) {
            bean = new ImBean();
            bean.setContent("sound".getBytes());
            bean.setSendId("e505be6118f0450897790b9b6a6d2de8".getBytes());
            bean.setType(1);
            datas.add(bean);
        }
        if(datas != null){
            mView.showChatContent(datas);
        }

    }

    @Override
    public void attachView(ChatContract.View view) {
        this.mView = view;

    }

    @Override
    public void detachView() {

    }
}
