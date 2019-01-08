package com.ioter.warehouse;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ioter.warehouse.common.AppCaughtException;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.di.component.AppComponent;
import com.ioter.warehouse.di.component.DaggerAppComponent;
import com.ioter.warehouse.di.module.AppModule;
import com.rscja.deviceapi.RFIDWithUHF;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.concurrent.ExecutorService;


public class AppApplication extends Application
{

    private AppComponent mAppComponent;

    private static ExecutorService mThreadPool;

    private static AppApplication mApplication;

    public static AppApplication getApplication()
    {
        return mApplication;
    }

    public AppComponent getAppComponent()
    {
        return mAppComponent;
    }

    public static ExecutorService getExecutorService()
    {
        return mThreadPool;
    }

    public static Gson mGson;

    public static Gson getGson()
    {
        return mGson;
    }

    public static RFIDWithUHF mReader; //RFID扫描

    public static Barcode2DWithSoft barcode2DWithSoft = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .build();
        mApplication = (AppApplication) mAppComponent.getApplication();
        mGson = mAppComponent.getGson();
        mThreadPool = mAppComponent.getExecutorService();

        initUHF();
    }

    public static class InitBarCodeTask extends AsyncTask<String, Integer, Boolean>
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
                reuslt = barcode2DWithSoft.open(mApplication);
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
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        Thread.setDefaultUncaughtExceptionHandler(new AppCaughtException());// 注册全局异常捕获
    }

    private int cycleCount = 3;//循环3次初始化
    //初始化RFID扫描
    public void initUHF()
    {
        try
        {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex)
        {
            ToastUtil.toast(ex.getMessage());
            return;
        }

        if (mReader != null)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!mReader.init())
                    {
                        /*Toast.makeText(mApplication, "init uhf fail,reset ...",
                                Toast.LENGTH_SHORT).show();*/
                        if(cycleCount > 0)
                        {
                            cycleCount--;
                            if (mReader != null)
                            {
                                mReader.free();
                            }
                            initUHF();

                        }
                    }else
                    {
                        /*Toast.makeText(mApplication, "init uhf success",
                                Toast.LENGTH_SHORT).show();*/
                    }
                }
            }).start();
        }
    }


}
