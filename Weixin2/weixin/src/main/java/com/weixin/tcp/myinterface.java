package com.weixin.tcp;

import com.weixin.bean.ImBean;

import java.io.IOException;

/**
 * Created by Mr.ZCM on 2016/7/25.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public interface myinterface {
    abstract void getMessage(ImBean bena);
    abstract void getVoice(byte b[]) throws IOException;
    abstract void getVideo(byte b[]);
    abstract  void getTalk(byte b[]);
    abstract void onLine();
    abstract void exit();
    abstract void getImageContent(byte[] img, int width, int height);

}
