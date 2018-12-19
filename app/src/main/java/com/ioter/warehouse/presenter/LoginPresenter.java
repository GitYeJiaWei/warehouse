package com.ioter.warehouse.presenter;


import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.common.rx.RxHttpReponseCompat;
import com.ioter.warehouse.common.rx.subscriber.ProgressSubcriber;
import com.ioter.warehouse.presenter.contract.LoginContract;

import javax.inject.Inject;


public class LoginPresenter extends BasePresenter<LoginContract.ILoginModel, LoginContract.LoginView>
{

    @Inject
    public LoginPresenter(LoginContract.ILoginModel iLoginModel, LoginContract.LoginView loginView)
    {
        super(iLoginModel, loginView);
    }

    public void login(String phone, String pwd)
    {
        mModel.login(phone, pwd).compose(RxHttpReponseCompat.<LoginBean>compatResult())
                .subscribe(new ProgressSubcriber<LoginBean>(AppApplication.getApplication(),mView)
                {

                    @Override
                    public void onNext(LoginBean loginBean)
                    {
                        if (loginBean != null)
                        {
                            mView.loginSuccess(loginBean);
                        } else
                        {
                            mView.checkError();
                        }
                    }
                });
    }



}
