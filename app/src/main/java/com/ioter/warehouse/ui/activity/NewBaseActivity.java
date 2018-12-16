package com.ioter.warehouse.ui.activity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.bean.BaseEpc;
import com.zebra.adc.decoder.Barcode2DWithSoft;

public class NewBaseActivity extends AppCompatActivity {
    protected Boolean IsFlushList = true; // 是否刷列表
    protected Object beep_Lock = new Object();
    protected ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
    public static Barcode2DWithSoft barcode2DWithSoft = null; //扫条码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            BaseEpc baseEpc = (BaseEpc) msg.obj;
            if(baseEpc!=null)
            {
                handleUi(baseEpc);
            }
        }
    };

    //处理ui
    public void handleUi(BaseEpc baseEpc)
    {
        synchronized (beep_Lock)
        {
            beep_Lock.notify();
        }
    }

    //配置读写器参数
    protected  void initSound()
    {
    }

    @Override
    public void onResume()
    {
        IsFlushList = true;
        initSound();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        IsFlushList = false;
        synchronized (beep_Lock)
        {
            beep_Lock.notifyAll();
        }
        super.onPause();
    }

    protected boolean loopFlag = false;

    class TagThread extends Thread
    {

        private int mBetween = 80;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {
            String strTid;
            String strResult;

            String[] res = null;
            while (loopFlag) {

                res = AppApplication.mReader.readTagFromBuffer();//.readTagFormBuffer();

                if (res != null) {

                    strTid = res[0];
                    if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Message msg = handler.obtainMessage();
                    BaseEpc baseEpc = new BaseEpc();
                    baseEpc._EPC = AppApplication.mReader.convertUiiToEPC(res[1]);
                    try
                    {
                        baseEpc.rssi = (new Double(Double.valueOf(res[2]))).intValue();
                    }catch (Exception e)
                    {

                    }
                    msg.obj = baseEpc;
                    handler.sendMessage(msg);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class InitBarCodeTask extends AsyncTask<String, Integer, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params)
        {

            if (barcode2DWithSoft == null)
            {
                barcode2DWithSoft = Barcode2DWithSoft.getInstance();
            }
            boolean reuslt = false;
            if (barcode2DWithSoft != null)
            {
                reuslt = barcode2DWithSoft.open(getApplicationContext());
            }
            return reuslt;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (result)
            {
                barcode2DWithSoft.setParameter(324, 1);
                barcode2DWithSoft.setParameter(300, 0); // Snapshot Aiming
                barcode2DWithSoft.setParameter(361, 0); // Image Capture Illumination

                // interleaved 2 of 5
                barcode2DWithSoft.setParameter(6, 1);
                barcode2DWithSoft.setParameter(22, 0);
                barcode2DWithSoft.setParameter(23, 55);

            } else
            {
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
    }

   /* @Override
    protected void onDestroy() {
        if (AppApplication.mReader != null)
        {
            AppApplication.mReader.free();
        }
        if (barcode2DWithSoft != null)
        {
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
    }*/

    @Override
    protected void onDestroy() {
        if (barcode2DWithSoft != null)
        {
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
    }
}
