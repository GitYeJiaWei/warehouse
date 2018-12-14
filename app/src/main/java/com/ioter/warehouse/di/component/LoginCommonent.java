package com.ioter.warehouse.di.component;

import com.ioter.warehouse.di.ActivityScope;
import com.ioter.warehouse.di.module.LoginModule;
import com.ioter.warehouse.ui.activity.LoginActivity;

import dagger.Component;

/**
 * @author YJW
 * @create 2018/6/14
 * @Describe
 */
@ActivityScope
@Component(modules = LoginModule.class,dependencies = AppComponent.class)
public interface LoginCommonent {
    void inject(LoginActivity loginActivity);
}
