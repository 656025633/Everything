package com.weixin.tcp;

/**
 * Created by zhao on 2016/7/24 0024.
 */
public interface DataTypeFormat {

    //音频
    byte   WAV = 0;
    byte   MP3 = 1;
    byte   WMA = 2;
    byte   OGG = 3;
    byte   ACC = 4;
    byte   APE = 5;
    //视频
    byte   MPEG = 0;
    byte   MPG = 1;
    byte   DAT = 2;
    byte   AVI = 3;
    byte   MOV = 4;
    byte   ASF = 5;
    byte   swf = 6;
    byte   MKV  = 7;
    byte   FLV  = 8;
    byte   MP4  = 9;

    //图片
    byte   BMP = 0;
    byte   JPEG  = 1;
    byte   PNG = 2;
    byte   GIF = 3;
    byte   JPG = 4;
}