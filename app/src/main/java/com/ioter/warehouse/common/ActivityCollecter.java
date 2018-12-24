package com.ioter.warehouse.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YJW on 2018/1/9.
 */

public class ActivityCollecter {
    public static List<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static  void finishAll(){
        for (Activity activity:activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
