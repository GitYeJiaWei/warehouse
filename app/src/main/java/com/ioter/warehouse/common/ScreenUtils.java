package com.ioter.warehouse.common;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ScreenUtils {

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view
     *            控件view
     * @param event
     *            焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText
                    ) {
                int[] location = { 0, 0 };
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    public static void hideKeyboard1(IBinder token, Activity activity) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** keyCode转换为字符 */
    public String keyCodeToChar(int code, boolean isShift){
        switch(code){

            case KeyEvent.KEYCODE_SHIFT_LEFT: return "";

            //数字键10个 + 符号10个
            case KeyEvent.KEYCODE_0: return isShift ? ")" : "0";
            case KeyEvent.KEYCODE_1: return isShift ? "!" : "1";
            case KeyEvent.KEYCODE_2: return isShift ? "@" : "2";
            case KeyEvent.KEYCODE_3: return isShift ? "#" : "3";
            case KeyEvent.KEYCODE_4: return isShift ? "$" : "4";
            case KeyEvent.KEYCODE_5: return isShift ? "%" : "5";
            case KeyEvent.KEYCODE_6: return isShift ? "^" : "6";
            case KeyEvent.KEYCODE_7: return isShift ? "&" : "7";
            case KeyEvent.KEYCODE_8: return isShift ? "*" : "8";
            case KeyEvent.KEYCODE_9: return isShift ? "(" : "9";

            //字母键26个小写 + 26个大写
            case KeyEvent.KEYCODE_A: return isShift ? "A" : "a";
            case KeyEvent.KEYCODE_B: return isShift ? "B" : "b";
            case KeyEvent.KEYCODE_C: return isShift ? "C" : "c";
            case KeyEvent.KEYCODE_D: return isShift ? "D" : "d";
            case KeyEvent.KEYCODE_E: return isShift ? "E" : "e";
            case KeyEvent.KEYCODE_F: return isShift ? "F" : "f";
            case KeyEvent.KEYCODE_G: return isShift ? "G" : "g";
            case KeyEvent.KEYCODE_H: return isShift ? "H" : "h";
            case KeyEvent.KEYCODE_I: return isShift ? "I" : "i";
            case KeyEvent.KEYCODE_J: return isShift ? "J" : "j";
            case KeyEvent.KEYCODE_K: return isShift ? "K" : "k";
            case KeyEvent.KEYCODE_L: return isShift ? "L" : "l";
            case KeyEvent.KEYCODE_M: return isShift ? "M" : "m";
            case KeyEvent.KEYCODE_N: return isShift ? "N" : "n";
            case KeyEvent.KEYCODE_O: return isShift ? "O" : "o";
            case KeyEvent.KEYCODE_P: return isShift ? "P" : "p";
            case KeyEvent.KEYCODE_Q: return isShift ? "Q" : "q";
            case KeyEvent.KEYCODE_R: return isShift ? "R" : "r";
            case KeyEvent.KEYCODE_S: return isShift ? "S" : "s";
            case KeyEvent.KEYCODE_T: return isShift ? "T" : "t";
            case KeyEvent.KEYCODE_U: return isShift ? "U" : "u";
            case KeyEvent.KEYCODE_V: return isShift ? "V" : "v";
            case KeyEvent.KEYCODE_W: return isShift ? "W" : "w";
            case KeyEvent.KEYCODE_X: return isShift ? "X" : "x";
            case KeyEvent.KEYCODE_Y: return isShift ? "Y" : "y";
            case KeyEvent.KEYCODE_Z: return isShift ? "Z" : "z";

            //符号键11个 + 11个
            case KeyEvent.KEYCODE_COMMA: return isShift ? "<" : ",";
            case KeyEvent.KEYCODE_PERIOD: return isShift ? ">" : ".";
            case KeyEvent.KEYCODE_SLASH: return isShift ? "?" : "/";
            case KeyEvent.KEYCODE_BACKSLASH: return isShift ? "|" : "\\";
            case KeyEvent.KEYCODE_APOSTROPHE: return isShift ? "\"" : "'";
            case KeyEvent.KEYCODE_SEMICOLON: return isShift ? ":" : ";";
            case KeyEvent.KEYCODE_LEFT_BRACKET: return isShift ? "{" : "[";
            case KeyEvent.KEYCODE_RIGHT_BRACKET: return isShift ? "}" : "]";
            case KeyEvent.KEYCODE_GRAVE: return isShift ? "~" : "`";
            case KeyEvent.KEYCODE_EQUALS: return isShift ? "+" : "=";
            case KeyEvent.KEYCODE_MINUS: return isShift ? "_" : "-";

            default: return "?";
        }
    }


}

