package com.ioter.warehouse.common.http;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.common.util.ACache;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/17.
 */

public class BaseUrlInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取老的url
        HttpUrl oldUrl = originalRequest.url();
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //根据头信息中配置的value,来匹配新的base_url地址
        String base_url = "http://" + ACache.get(AppApplication.getApplication()).getAsString("ip") + ":8097/api/";
        HttpUrl baseURL = HttpUrl.parse(base_url);
        //重建新的HttpUrl，需要重新设置的url部分
        HttpUrl newHttpUrl = oldUrl.newBuilder()
                .scheme(baseURL.scheme())//http协议如：http或者https
                .host(baseURL.host())//主机地址
                .port(baseURL.port())//端口
                .build();
        //获取处理后的新newRequest
        Request newRequest = builder.url(newHttpUrl).build();
        return chain.proceed(newRequest);
    }
}
