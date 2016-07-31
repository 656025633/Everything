package com.weixin.tcp;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Mr.ZCM on 2016/7/26.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class AudioFile {
     static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/voice.mp3";
     static MediaRecorder recoder ;
     MediaPlayer player;
    //开始录音
    public static  void record() {
        recoder = new MediaRecorder();
        recoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recoder.setOutputFile(filePath);
        try {
            recoder.prepare();
            recoder.start();
            Log.d("random","保存录音");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
