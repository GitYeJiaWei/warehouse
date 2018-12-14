package com.ioter.warehouse.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.di.component.AppComponent;
import com.ioter.warehouse.presenter.BasePresenter;
import com.ioter.warehouse.ui.BaseView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView
{

    private Unbinder mUnbinder;

    protected AppApplication mApplication;

    private Toast mToast = null;
    private ProgressDialog waitDialog = null;


    @Inject
    public T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(setLayout());

        mUnbinder = ButterKnife.bind(this);
        this.mApplication = (AppApplication) getApplication();

        setupAcitivtyComponent(mApplication.getAppComponent());

        init();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mUnbinder!=null&&mUnbinder != Unbinder.EMPTY)
        {

            mUnbinder.unbind();
        }
        if (waitDialog != null)
        {
            waitDialog = null;
        }
    }


//    protected  void  startActivity(Class c){
//
//        this.startActivity(new Intent(this,c));
//    }
//
//


    public abstract int setLayout();

    public abstract void setupAcitivtyComponent(AppComponent appComponent);


    public abstract void init();


    @Override
    public void showLoading()
    {
        if (waitDialog == null)
        {
            waitDialog = new ProgressDialog(this);
        }
        waitDialog.setMessage("加载中...");
        waitDialog.show();
    }

    @Override
    public void showError(String msg)
    {
        if (waitDialog != null)
        {
            waitDialog.setMessage(msg);
            waitDialog.show();
        }
    }

    @Override
    public void dismissLoading()
    {
        if (waitDialog != null && waitDialog.isShowing())
        {
            waitDialog.dismiss();
        }
    }




}
