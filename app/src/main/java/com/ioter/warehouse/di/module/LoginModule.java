package com.ioter.warehouse.di.module;

import com.ioter.warehouse.data.LoginModel;
import com.ioter.warehouse.data.http.ApiService;
import com.ioter.warehouse.presenter.contract.LoginContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author YJW
 * @create 2018/6/14
 * @Describe
 */
@Module
public class LoginModule {
    private LoginContract.LoginView mView;

    public LoginModule(LoginContract.LoginView loginView){
        this.mView = loginView;
    }

    @Provides
    public LoginContract.LoginView provideView()
    {

        return mView;
    }


    @Provides
    public LoginContract.ILoginModel privodeModel(ApiService apiService)
    {

        return new LoginModel(apiService);
    }


}
