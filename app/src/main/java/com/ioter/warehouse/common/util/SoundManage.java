package com.ioter.warehouse.common.util;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ioter.warehouse.R;


public class SoundManage
{

    private  static int successID=-1;
    private  static int failureID=-1;
    private  static SoundPool soundPool=null;
    public static void  PlaySound(Context c, SoundType type)
    {
        soundPool= getSoundPool();
        if(soundPool==null)
            return;

        int id=-1;

        if(type== SoundType.SUCCESS)
        {
            if(successID==-1) {
                successID = soundPool.load(c, R.raw.barcodebeep, 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            id=successID;
        }
        else  if(type== SoundType.FAILURE)
        {
            if(failureID==-1)
            {
                //第一个参数为id
                //第二个和第三个参数为左右声道的音量控制
                //第四个参数为优先级，由于只有这一个声音，因此优先级在这里并不重要
                //第五个参数为是否循环播放，0为不循环，-1为循环
                //最后一个参数为播放比率，从0.5到2，一般为1，表示正常播放。
                failureID =soundPool.load(c, R.raw.serror,1);
            }
            id=failureID;
        }
        if(id!=-1)
            soundPool.play(id,1, 1, 0, 0, 1);

    }

    private static SoundPool getSoundPool()
    {
        if(soundPool==null)
        {
            soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        }
        return soundPool;
    }




    public  enum SoundType{
       FAILURE,SUCCESS
    }


}
