package com.weixin.tcp;

/**
 * Created by zhao on 2016/7/24 0024.
 */
public interface DataType {

    //现场数据上传数据类型
    byte UP_scene_Video = 0;     //现场录像
    byte UP_scene_Images = 1;    //现场图像
    byte UP_scene_Voice = 2;    //现场语音


    //IM上传数据类型
    byte UP_Chat_Word  = 0;     //聊天文字
    byte UP_Chat_Voice = 1;     //聊天语音  对讲
    byte UP_Chat_Video = 2;     //聊天录像
    byte UP_Chat_Images = 3;    //聊天图片



    //数据下载数据类型
    byte Down_Lost_Word = 0;     //失物文字
    byte Down_Lost_Video = 1;     //失物录像
    byte Down_Lost_Images = 2;    //失物图像
    byte Down_Lost_Voice = 4;    //失物语音



    byte Down_Chat_Word  = 5;     //聊天文字
    byte Down_Chat_Voice = 6;     //聊天语音
    byte Down_Chat_Video = 7;     //聊天录像
    byte Down_Chat_Images = 8;    //聊天图片

}