package com.ioter.warehouse.common.util;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.bean.StoresBean;
import com.ioter.warehouse.ui.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class ACacheUtils
{

    public static final String USER_NAME = "username";

    public static void saveUser(LoginBean loginBean)
    {
        ACache.get(AppApplication.getApplication()).put(USER_NAME, loginBean);
    }

    public static String getUserName()
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                return loginBean.getUserName();
            }
        }
        return null;
    }

    public static String getUserId()
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                return loginBean.getId();
            }
        }
        return null;
    }

    public static String getWareIdByWhCode(String whCode)
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                List<StoresBean> listWh = loginBean.getStores();
                if (listWh != null && listWh.size() > 0)
                {
                    List<String> results = new ArrayList<>();
                    for (StoresBean item : listWh)
                    {
                        String itemWhCode = item.getStoreName();
                        if (whCode.equals(itemWhCode))
                        {
                            return  item.getId();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<String> getWhCodeWithUser()
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                List<StoresBean> listWh = loginBean.getStores();
                if (listWh != null && listWh.size() > 0)
                {
                    List<String> results = new ArrayList<>();
                    for (StoresBean item : listWh)
                    {
                        String whCode = item.getStoreName();
                        if (whCode != null)
                        {
                            results.add(whCode);
                        }
                    }
                    return results;
                }
            }
        }
        return null;
    }
}
