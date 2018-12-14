package com.ioter.warehouse.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.common.util.VariableConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 全局异常捕获
 */
public class AppCaughtException implements UncaughtExceptionHandler
{
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;// 系统的异常捕获对象

    public AppCaughtException()
    {
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        savaInfoToSD(AppApplication.getApplication(), ex);
        if (ex == null && mDefaultUncaughtExceptionHandler != null)
        {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
        } else
        {
            Intent intent = new Intent();
            intent.setComponent(
                    new ComponentName(VariableConstant.APP_PACKAGE_MAIN, "com.ioter.clothesstrore.video.floatUtil.EpcShowMainActivity"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppApplication.getApplication().startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private String savaInfoToSD(Context context, Throwable ex)
    {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context)
                .entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        sb.append(obtainExceptionInfo(ex));

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            String dirPath = Environment.getExternalStorageDirectory()
                    .getPath().toString()
                    + "/";
            File dir = new File(dirPath + "crashForDeveloper" + File.separator);
            if (!dir.exists())
            {
                dir.mkdir();
            }

            File[] files = dir.listFiles();
            if (files.length > 20)
            {
                // delete when record over 20
                for (int i = 0; i < files.length; i++)
                {
                    files[i].delete();
                }
            }
            try
            {
                fileName = dir.toString() + File.separator
                        + paserTime(System.currentTimeMillis()) + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        return fileName;
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try
        {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);

        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);

        return map;
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds)
    {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));

        return times;
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable)
    {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        // Log.e(TAG, mStringWriter.toString());
        return mStringWriter.toString();
    }


}