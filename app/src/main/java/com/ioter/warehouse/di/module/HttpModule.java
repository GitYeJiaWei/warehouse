package com.ioter.warehouse.di.module;

import android.app.Application;

import com.google.gson.Gson;
import com.ioter.warehouse.common.http.BaseUrlInterceptor;
import com.ioter.warehouse.data.http.ApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class HttpModule
{
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Application application, Gson gson)
    {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();


/*        if (BuildConfig.DEBUG)
        {
            // log用拦截器
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.addInterceptor(logging);

        }*/


        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
//        SSLSocketFactory sslSocketFactory = null;

        return builder
                .addInterceptor(new BaseUrlInterceptor())
                //添加应用拦截器
                // 连接超时时间设置
                .connectTimeout(20, TimeUnit.SECONDS)
                // 读取超时时间设置
                //.readTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

                .build();


    }


    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient)
    {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);


        return builder.build();

    }


    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit)
    {

        return retrofit.create(ApiService.class);
    }


}
