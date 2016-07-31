package com.weixin.tcp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mr.ZCM on 2016/7/26.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class TalkUitl {
    private AudioRecord recorder;
    private byte m_in_bytes[];
    private LinkedList<byte[]> m_in_q;
    private ProducerTool pro = ProducerTool.getInstance();
    Thread t;
    // 获得录音缓冲区大小
    int bufferSize = AudioRecord.getMinBufferSize(8000,
            AudioFormat.CHANNEL_CONFIGURATION_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    public void say(final boolean stoptalk, final String userId, final List<String> reUser, final byte dataTypeFormat){
        //UserIMVoiceNow(String uid, List<String> reUser,byte dataTypeFormat, byte[] Voicecontent)
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                // 获得录音机对象
                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                        AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize);
                m_in_bytes = new byte [bufferSize] ;
                m_in_q=new LinkedList<byte[]>();
                byte [] bytes_pkg ;
                recorder.startRecording();// 开始录音
                byte[] readBuffer = new byte[640];// 录音缓冲区
                int length = 0;
                while (stoptalk) {
                    recorder.read(m_in_bytes, 0, bufferSize);
                    bytes_pkg = m_in_bytes.clone() ;
                    if(m_in_q.size() >= 2)
                    {
                        byte[] content = new byte[m_in_q.removeFirst().length];
                        System.arraycopy(m_in_q.removeFirst(), 0, content , 0, content .length);
                        //发送  content
                        try {
                            pro.produceMessage(TCPCli.UserIMVoiceNow(userId, reUser, dataTypeFormat,content));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    m_in_q.add(bytes_pkg) ;
                }
                recorder.stop();
            }
        });
    }
    public void receive(byte VoiceContent[]){
        // 获得音频缓冲区大小
        int bufferSize = AudioTrack.getMinBufferSize(
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        // 获得音轨对象
        AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                AudioTrack.MODE_STREAM);

        //设置喇叭音量
        player.setStereoVolume(1.0f, 1.0f);
        byte [] bytes_pkg = null ;
        // 开始播放声音
        player.play();
        byte[] audio = new byte[160];// 音频读取缓存

        bytes_pkg = VoiceContent.clone() ;
        player.write(bytes_pkg,0,bytes_pkg.length);

       /* int length = VoiceContent.length ;
        int offsize = 0;
        while (offsize <length  ) {

            System.arraycopy(VoiceContent,offsize , audio ,0, audio.length);
            offsize += audio .length ;

            bytes_pkg = audio.clone() ;
            player.write(bytes_pkg,0,bytes_pkg.length);
        }*/
        player.stop();

    }

}
