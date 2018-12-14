package com.ioter.warehouse.common.util;

import android.util.Log;

public class DebugUtil
{
    public static void printd(String strTag, String strMsg)
    {
        Log.d(strTag, strMsg);
    }

    public static void printe(String strTag, String strMsg)
    {
        Log.e(strTag, strMsg);
    }

    public static void printe(String strTag, Throwable throwable)
    {
        String strMsg = "Exception：" + throwable;
        if (throwable != null)
        {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0)
            {
                for (StackTraceElement stackTraceElement : stackTrace)
                {
                    strMsg = strMsg + "\n" + stackTraceElement;
                }
            }
        }
        Log.e(strTag, strMsg);
    }
}