package com.ioter.warehouse.data;

import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.data.http.ApiService;
import com.ioter.warehouse.presenter.contract.LoginContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author YJW
 * @create 2018/7/23
 * @Describe
 */
public class LoginModel implements LoginContract.ILoginModel{
    private ApiService mapiService;

    public LoginModel(ApiService apiService){
        this.mapiService = apiService;
    }

    @Override
    public Observable<BaseBean<LoginBean>> login(String phone, String pwd) {


        Map<String,String> params = new HashMap<>();
        params.put("userName",phone);
        params.put("password",pwd);


       /* String data = AppApplication.getGson().toJson(params);
        long time = System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        String timestamp=String.valueOf(time);
        String m5 = "timestamp" + timestamp + "secret" + "iottest" + "data" + data;
        String sign= DataUtil.md5(m5);
        Map<String,String> param = new HashMap<>();
        param.put("data",data);
        param.put("timestamp",timestamp+"");
        param.put("sign",sign);*/

        return mapiService.login(params);
    }
}