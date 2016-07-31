package com.weixin.tcp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by Mr.ZCM on 2016/7/27.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain: 用于处理视频的工具类
 */
public class VideoUtil {
    private static String videopath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recordVideo/"+System.currentTimeMillis() + ".mp4";

    /**
     *调用系统进行视频录制
     * @param videoPath
     */
    public static Intent recordVideo(String videoPath){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //添加参数，存储路径
        File file = new File(videopath);
        Log.d("random","videopath:"+videopath);
        Uri uri =Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        //设置视频拍摄时长
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
        //最大存储
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,1024*1024*10);
        //设置质量
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,100);
        return intent;
      //  startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     *
     * @param videoPath
     */
    public void playVideo(String videoPath){

    }
}
