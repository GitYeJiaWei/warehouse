package com.ioter.warehouse.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.ioter.warehouse.data.http.ApiService;
import com.ioter.warehouse.di.module.AppModule;
import com.ioter.warehouse.di.module.HttpModule;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;


@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent
{

    public Application getApplication();

    public ExecutorService getExecutorService();

    public Gson getGson();

    public ApiService getApiService();

    public OkHttpClient getOkHttpClient();

}
