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

    //用户名
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

    //用户ID
    public static String getUserId()
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                return loginBean.getUserId();
            }
        }
        return null;
    }

    //仓库id
    public static String getWareIdByWhCode(String whCode)
    {
        String result = ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.USER_NAME);
        if (result != null)
        {
            LoginBean loginBean = AppApplication.getGson().fromJson(result, LoginBean.class);
            if (loginBean != null)
            {
                List<StoresBean> listWh = loginBean.getListWarehouse();
                if (listWh != null && listWh.size() > 0)
                {
                    List<String> results = new ArrayList<>();
                    for (StoresBean item : listWh)
                    {
                        String itemWhCode = item.getWhName();
                        if (whCode.equals(itemWhCode))
                        {
                            return  item.getWhId();
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
                List<StoresBean> listWh = loginBean.getListWarehouse();
                if (listWh != null && listWh.size() > 0)
                {
                    List<String> results = new ArrayList<>();
                    for (StoresBean item : listWh)
                    {
                        String whCode = item.getWhName();
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
