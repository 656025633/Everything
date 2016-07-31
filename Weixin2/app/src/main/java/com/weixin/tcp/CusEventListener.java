package com.weixin.tcp;

import android.util.Log;

import com.weixin.bean.ImBean;

import java.io.IOException;
import java.util.EventListener;

/**
 * Created by zhao on 2016/7/25 0025.
 */
public class CusEventListener implements EventListener, MessageType, DataType {
    myinterface mMyinterface ;
    public void setContext(myinterface context){
        this.mMyinterface = context;
    }

    //事件发生后的回调方法
    public void fireCusEvent(CusEvent e) {
        ProducerTool Object = (ProducerTool) e.getSource();

        byte[] bytes = Object.getGetUserMessage();

        if (bytes != null && bytes.length > 0) {

            byte MessageType = bytes[0];
            byte[] snedUid = new byte[32];
            System.arraycopy(bytes, 1, snedUid, 0, snedUid.length);

            switch (MessageType) {
                //用户登录请求
                case USER_LOGIN_REQUEST:
                    String loginUserId = new String(snedUid);
                    System.out.println("loginUserId:" + loginUserId);
                    mMyinterface.onLine();
                    break;
                //用户登录退出请求
                case USER_LOGINOUT_REQUEST:
                    String loginOutUserId = new String(snedUid);
                    System.out.println("loginOutUserId:" + loginOutUserId);
                    break;
                //IM消息转发
                case DATA_REQUEST_IM:
                    byte dataType = bytes[1 + snedUid.length];
                    switch (dataType) {
                        //聊天文字
                        case UP_Chat_Word:
                            byte[] fontType = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1, fontType, 0, fontType.length);
                            byte[] fontSize = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + fontType.length, fontType, 0, fontSize.length);
                            byte[] fontContentSize = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + fontType.length + fontSize.length, fontContentSize, 0, fontContentSize.length);
                            int fSize = ByteObjConverter.byteArrayToInt(fontContentSize);
                            byte[] fontContent = new byte[fSize];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + fontType.length + fontSize.length + fontContentSize.length, fontContent, 0, fontContent.length);
                            String userContent = new String(fontContent);
                            System.out.println(userContent);
                            ImBean bean = new ImBean();
                            bean.setType(0);
                            bean.setSendId(snedUid);
                            bean.setContent(fontContent);
                            mMyinterface.getMessage(bean);
                            Log.d("random","聊天文字接收成功");
                            break;
                        //聊天语音
                        case UP_Chat_Voice:
                            byte[]  voiceType = new byte[1];
                            System.arraycopy(bytes, 1 + snedUid.length + 1, voiceType, 0, voiceType.length);
                            byte[]  voiceContentSize = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + voiceType.length, voiceContentSize, 0, voiceContentSize.length);
                            int vSize = ByteObjConverter.byteArrayToInt(voiceContentSize);
                            byte[] VoiceContent = new byte[vSize];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + voiceType.length+voiceContentSize.length, VoiceContent, 0, VoiceContent.length);
                            try {
                                ImBean voice = new ImBean();
                                voice.setType(1);
                                voice.setSendId(snedUid);
                                voice.setContent(VoiceContent);
                                mMyinterface.getVoice(voice);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            Log.d("random","聊天音频接收成功");
                            break;
                        //聊天录像
                        case UP_Chat_Video:
                            byte[]  videoType = new byte[1];
                            System.arraycopy(bytes, 1 + snedUid.length + 1, videoType, 0, videoType.length);
                            byte[]  videoContentSize = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + videoType.length, videoContentSize, 0, videoContentSize.length);
                            int videoSize = ByteObjConverter.byteArrayToInt(videoContentSize);
                            byte[] VideoContent = new byte[videoSize];
                            System.arraycopy(bytes, 1 + snedUid.length + 1 + videoType.length+videoContentSize.length, VideoContent, 0, VideoContent.length);
                            mMyinterface.getVideo(VideoContent);
                            Log.d("random","聊天视频接收成功");
                            break;
                        //聊天图片
                        case UP_Chat_Images:
                            byte[]  imageType = new byte[1];
                            System.arraycopy(bytes, 1 + snedUid.length + 1, imageType, 0, imageType.length);

                            byte[]  imageHeight = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1+imageType.length, imageHeight, 0, imageHeight.length);
                            int imHeight = ByteObjConverter.byteArrayToInt(imageHeight);

                            byte[]  imageWith = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1+imageType.length+imageHeight.length, imageWith, 0, imageWith.length);
                            int imWith = ByteObjConverter.byteArrayToInt(imageWith);


                            byte[]  imageContentSize = new byte[4];
                            System.arraycopy(bytes, 1 + snedUid.length + 1+imageType.length+imageHeight.length+imageWith.length, imageContentSize, 0, imageContentSize.length);
                            int imContentSize = ByteObjConverter.byteArrayToInt(imageContentSize);

                            byte[]  ImageContent = new byte[imContentSize];
                            System.arraycopy(bytes, 1 + snedUid.length + 1+imageType.length+imageHeight.length+imageWith.length+imageContentSize.length, ImageContent, 0, ImageContent.length);
                            mMyinterface.getImageContent(ImageContent, imWith, imHeight);
                            break;
                    }
                    break;
                //对讲
                case DATA_REQUEST_IM_NOW:
                    byte daType = bytes[1 + snedUid.length];
                    byte[]  voiceType = new byte[1];
                    System.arraycopy(bytes, 1 + snedUid.length + 1, voiceType, 0, voiceType.length);
                    byte[]  voiceContentSize = new byte[4];
                    System.arraycopy(bytes, 1 + snedUid.length + 1 + voiceType.length, voiceContentSize, 0, voiceContentSize.length);
                    int vSize = ByteObjConverter.byteArrayToInt(voiceContentSize);
                    byte[] VoiceContent = new byte[vSize];
                    System.arraycopy(bytes, 1 + snedUid.length + 1 + voiceType.length+voiceContentSize.length, VoiceContent, 0, VoiceContent.length);
                    mMyinterface.getTalk(VoiceContent.clone());
                    break;
                //现场记录
                case DATA_REQUEST_Oper:

            }


        }
    }
}
