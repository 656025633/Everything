package com.weixin.tcp;

import java.util.List;

/**
 * Created by zhao on 2016/7/25 0025.
 */
public class TCPCli implements MessageType, DataType {

    //用户登录
    public static byte[] UserLogin(String uid) {

        byte[] mgType = new byte[1];
        mgType[0] = USER_LOGIN_REQUEST;
        //mgType[0] = USER_LOGINOUT_REQUEST;

        byte[] SnedUid = uid.getBytes();

        byte[] sendContentByte = new byte[mgType.length + SnedUid.length];
        System.arraycopy(mgType, 0, sendContentByte, 0, 1);
        System.arraycopy(SnedUid, 0, sendContentByte, 1, SnedUid.length);

        int HeadLenth = mgType.length + SnedUid.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);

        int test = ByteObjConverter.byteArrayToInt(headLenth);

        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);
        return sendByte.clone();
    }

    //用户退出
    public static byte[] UserLoginOut(String uid) {

        byte[] mgType = new byte[1];
        mgType[0] = USER_LOGINOUT_REQUEST;

        byte[] SnedUid = uid.getBytes();

        byte[] sendContentByte = new byte[mgType.length + SnedUid.length];
        System.arraycopy(mgType, 0, sendContentByte, 0, 1);
        System.arraycopy(SnedUid, 0, sendContentByte, 1, SnedUid.length);

        int HeadLenth = mgType.length + SnedUid.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);

        int test = ByteObjConverter.byteArrayToInt(headLenth);

        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);
        return sendByte.clone();
    }

    //用户发送文字
    public static byte[] UserIMWord(String uid, List<String> reUser, String content) {

        byte[] mgType = new byte[1];
        mgType[0] = DATA_REQUEST_IM;


        byte[] SnedUid = uid.getBytes();

        int receiveNumber = reUser.size();
        byte[] ReceiveNumber = ByteObjConverter.intToByteArray1(receiveNumber);

        byte[] ReceiveUids = new byte[32 * receiveNumber];
        int offsize = 0;
        for (int i = 0; i < reUser.size(); i++) {

            byte[] res = reUser.get(i).getBytes();
            System.arraycopy(res, 0, ReceiveUids, offsize, res.length);
            offsize += res.length;
        }

        byte[] DataType = new byte[1];
        DataType[0] = UP_Chat_Word;


        byte[] FontType = new byte[4];

        int fontSize = 15;
        byte[] FontSize = ByteObjConverter.intToByteArray1(fontSize);

        byte[] FontContent = content.getBytes();

        int fontContentSize = FontContent.length;
        byte[] FontContentSize = ByteObjConverter.intToByteArray1(fontContentSize);


        byte[] sendContentByte = new byte[mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + FontType.length + FontSize.length + FontContentSize.length + FontContent.length];
        System.arraycopy(mgType, 0, sendContentByte, 0, mgType.length);
        System.arraycopy(SnedUid, 0, sendContentByte, mgType.length, SnedUid.length);
        System.arraycopy(ReceiveNumber, 0, sendContentByte, mgType.length + SnedUid.length, ReceiveNumber.length);
        System.arraycopy(ReceiveUids, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length, ReceiveUids.length);
        System.arraycopy(DataType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length, DataType.length);
        System.arraycopy(FontType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length, FontType.length);
        System.arraycopy(FontSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + FontType.length, FontSize.length);
        System.arraycopy(FontContentSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + FontType.length + FontSize.length, FontContentSize.length);
        System.arraycopy(FontContent, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + FontType.length + FontSize.length + FontContentSize.length, FontContent.length);


        int HeadLenth = sendContentByte.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);


        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

        return sendByte.clone();
    }

    //用户发送语音
    public static byte[] UserIMVoice(String uid, List<String> reUser,byte dataTypeFormat, byte[] Voicecontent) {

        byte[] mgType = new byte[1];
        mgType[0] = DATA_REQUEST_IM;


        byte[] SnedUid = uid.getBytes();

        int receiveNumber = reUser.size();
        byte[] ReceiveNumber = ByteObjConverter.intToByteArray1(receiveNumber);

        byte[] ReceiveUids = new byte[32 * receiveNumber];
        int offsize = 0;
        for (int i = 0; i < reUser.size(); i++) {

            byte[] res = reUser.get(i).getBytes();
            System.arraycopy(res, 0, ReceiveUids, offsize, res.length);
            offsize += res.length;
        }

        byte[] DataType = new byte[1];
        DataType[0] = UP_Chat_Voice;


        byte[] VoiceType = new byte[1];
        VoiceType[0] = dataTypeFormat;

        int voiceContentSize = Voicecontent.length;
        byte[] VoiceContentSize = ByteObjConverter.intToByteArray1(voiceContentSize);


        byte[] VoiceContent = Voicecontent;


        byte[] sendContentByte = new byte[mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length + VoiceContentSize.length + VoiceContent.length];

        System.arraycopy(mgType, 0, sendContentByte, 0, mgType.length);
        System.arraycopy(SnedUid, 0, sendContentByte, mgType.length, SnedUid.length);
        System.arraycopy(ReceiveNumber, 0, sendContentByte, mgType.length + SnedUid.length, ReceiveNumber.length);
        System.arraycopy(ReceiveUids, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length, ReceiveUids.length);
        System.arraycopy(DataType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length, DataType.length);
        System.arraycopy(VoiceType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length, VoiceType.length);
        System.arraycopy(VoiceContentSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length, VoiceContentSize.length);
        System.arraycopy(VoiceContent, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length + VoiceContentSize.length, VoiceContent.length);


        int HeadLenth = sendContentByte.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);


        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

        return sendByte.clone();
    }

    //用户发送对讲语音
    public static byte[] UserIMVoiceNow(String uid, List<String> reUser,byte dataTypeFormat, byte[] Voicecontent) {

        byte[] mgType = new byte[1];
        mgType[0] = DATA_REQUEST_IM;


        byte[] SnedUid = uid.getBytes();

        int receiveNumber = reUser.size();
        byte[] ReceiveNumber = ByteObjConverter.intToByteArray1(receiveNumber);

        byte[] ReceiveUids = new byte[32 * receiveNumber];
        int offsize = 0;
        for (int i = 0; i < reUser.size(); i++) {

            byte[] res = reUser.get(i).getBytes();
            System.arraycopy(res, 0, ReceiveUids, offsize, res.length);
            offsize += res.length;
        }

        byte[] DataType = new byte[1];
        DataType[0] = DATA_REQUEST_IM_NOW;


        byte[] VoiceType = new byte[1];
        VoiceType[0]=dataTypeFormat;

        int voiceContentSize = Voicecontent.length;
        byte[] VoiceContentSize = ByteObjConverter.intToByteArray1(voiceContentSize);


        byte[] VoiceContent = Voicecontent;


        byte[] sendContentByte = new byte[mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length + VoiceContentSize.length + VoiceContent.length];

        System.arraycopy(mgType, 0, sendContentByte, 0, mgType.length);
        System.arraycopy(SnedUid, 0, sendContentByte, mgType.length, SnedUid.length);
        System.arraycopy(ReceiveNumber, 0, sendContentByte, mgType.length + SnedUid.length, ReceiveNumber.length);
        System.arraycopy(ReceiveUids, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length, ReceiveUids.length);
        System.arraycopy(DataType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length, DataType.length);
        System.arraycopy(VoiceType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length, VoiceType.length);
        System.arraycopy(VoiceContentSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length, VoiceContentSize.length);
        System.arraycopy(VoiceContent, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VoiceType.length + VoiceContentSize.length, VoiceContent.length);


        int HeadLenth = sendContentByte.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);


        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

        return sendByte.clone();
    }

    //用户发送视频
    public static byte[] UserIMVideo(String uid, List<String> reUser,byte dataTypeFormat, byte[] Videocontent) {

        byte[] mgType = new byte[1];
        mgType[0] = DATA_REQUEST_IM;


        byte[] SnedUid = uid.getBytes();

        int receiveNumber = reUser.size();
        byte[] ReceiveNumber = ByteObjConverter.intToByteArray1(receiveNumber);

        byte[] ReceiveUids = new byte[32 * receiveNumber];
        int offsize = 0;
        for (int i = 0; i < reUser.size(); i++) {

            byte[] res = reUser.get(i).getBytes();
            System.arraycopy(res, 0, ReceiveUids, offsize, res.length);
            offsize += res.length;
        }

        byte[] DataType = new byte[1];
        DataType[0] = UP_Chat_Video;

        byte[] VideoType = new byte[1];
        VideoType[0]=dataTypeFormat;


        int videoContentSize = Videocontent.length;
        byte[] VideoContentSize = ByteObjConverter.intToByteArray1(videoContentSize);


        byte[] VideoContent = Videocontent;


        byte[] sendContentByte = new byte[mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VideoType.length + VideoContentSize.length + VideoContent.length];
        System.arraycopy(mgType, 0, sendContentByte, 0, mgType.length);
        System.arraycopy(SnedUid, 0, sendContentByte, mgType.length, SnedUid.length);
        System.arraycopy(ReceiveNumber, 0, sendContentByte, mgType.length + SnedUid.length, ReceiveNumber.length);
        System.arraycopy(ReceiveUids, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length, ReceiveUids.length);
        System.arraycopy(DataType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length, DataType.length);
        System.arraycopy(VideoType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length, VideoType.length);
        System.arraycopy(videoContentSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VideoType.length, VideoContentSize.length);
        System.arraycopy(VideoContent, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + VideoType.length + VideoContentSize.length, VideoContent.length);


        int HeadLenth = sendContentByte.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);


        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

        return sendByte.clone();
    }

    //用户发送视频
    public static byte[] UserIMImages(String uid, List<String> reUser,byte dataTypeFormat, int imageHeight, int imageWith, byte[] imagescontent) {

        byte[] mgType = new byte[1];
        mgType[0] = DATA_REQUEST_IM;


        byte[] SnedUid = uid.getBytes();

        int receiveNumber = reUser.size();
        byte[] ReceiveNumber = ByteObjConverter.intToByteArray1(receiveNumber);

        byte[] ReceiveUids = new byte[32 * receiveNumber];
        int offsize = 0;
        for (int i = 0; i < reUser.size(); i++) {

            byte[] res = reUser.get(i).getBytes();
            System.arraycopy(res, 0, ReceiveUids, offsize, res.length);
            offsize += res.length;
        }

        byte[] DataType = new byte[1];
        DataType[0] = UP_Chat_Images;

        byte[] ImageType = new byte[1];
        ImageType[0]= dataTypeFormat;


        int imageHeights = imageHeight;
        byte[] ImageHeights = ByteObjConverter.intToByteArray1(imageHeights);

        int imageWiths = imageWith;
        byte[] ImageWiths = ByteObjConverter.intToByteArray1(imageWiths);

        int imageContentSize = imagescontent.length;
        byte[] ImageContentSize = ByteObjConverter.intToByteArray1(imageContentSize);

        byte[] ImageContent = imagescontent;


        byte[] sendContentByte = new byte[mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + ImageType.length + ImageHeights.length + ImageWiths.length + ImageContentSize.length + ImageContent.length];
        System.arraycopy(mgType, 0, sendContentByte, 0, mgType.length);
        System.arraycopy(SnedUid, 0, sendContentByte, mgType.length, SnedUid.length);
        System.arraycopy(ReceiveNumber, 0, sendContentByte, mgType.length + SnedUid.length, ReceiveNumber.length);
        System.arraycopy(ReceiveUids, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length, ReceiveUids.length);
        System.arraycopy(DataType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length, DataType.length);
        System.arraycopy(ImageType, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length, ImageType.length);
        System.arraycopy(ImageHeights, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + ImageType.length, ImageHeights.length);
        System.arraycopy(ImageWiths, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + ImageType.length + ImageHeights.length, ImageWiths.length);
        System.arraycopy(ImageContentSize, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + ImageType.length + ImageHeights.length + ImageWiths.length, ImageContentSize.length);
        System.arraycopy(ImageContent, 0, sendContentByte, mgType.length + SnedUid.length + ReceiveNumber.length + ReceiveUids.length + DataType.length + ImageType.length + ImageHeights.length + ImageWiths.length + ImageContentSize.length, ImageContent.length);


        int HeadLenth = sendContentByte.length + 2;
        byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);


        byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

        short Crc16 = CRC16.CRC16(sendContentByte);
        byte[] crc16 = ByteObjConverter.putShort(Crc16);

        byte[] sendByte = new byte[HeadLenth + 4 + 4];
        System.arraycopy(head, 0, sendByte, 0, 4);
        System.arraycopy(headLenth, 0, sendByte, 4, 4);
        System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
        System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

        return sendByte.clone();
    }


    //// TODO: 2016/7/25 0025

}
