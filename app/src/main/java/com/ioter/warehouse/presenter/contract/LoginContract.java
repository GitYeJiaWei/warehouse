package com.ioter.warehouse.presenter.contract;


import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.ui.BaseView;

import io.reactivex.Observable;

public interface LoginContract
{


    public interface ILoginModel
    {
        Observable<BaseBean<LoginBean>> login(String phone, String pwd);
    }

    public interface LoginView extends BaseView
    {

        void checkError();


        void loginSuccess(LoginBean bean);
    }
}
