package com.ioter.warehouse.common.util;

import android.os.Environment;

import java.io.File;

public class VariableConstant
{

    public static final String APP_PACKAGE_MAIN = "com.ioter.base";
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "base" + File.separator;
}