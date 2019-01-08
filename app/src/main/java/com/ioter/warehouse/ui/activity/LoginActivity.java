package com.ioter.warehouse.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.common.util.UIConstant;
import com.ioter.warehouse.di.component.AppComponent;
import com.ioter.warehouse.di.component.DaggerLoginCommonent;
import com.ioter.warehouse.di.module.LoginModule;
import com.ioter.warehouse.presenter.LoginPresenter;
import com.ioter.warehouse.presenter.contract.LoginContract;
import com.ioter.warehouse.ui.dialog.ServerIpDialog;
import com.ioter.warehouse.ui.widget.LoadingButton;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {


    @BindView(R.id.txt_mobi)
    EditText txtMobi;
    @BindView(R.id.txt_password)
    EditText txtPassword;
    @BindView(R.id.btn_login)
    LoadingButton btnLogin;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.rfid)
    ImageView rfid;
    public static final String USER_NAME = "username";


    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerLoginCommonent.builder().appComponent(appComponent).loginModule(new LoginModule(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        initview();

        //初始化二维码扫描头
        if (Build.VERSION.SDK_INT > 21) {

            //扫条码 需要相机对应用开启相机和存储权限；
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
            //读写内存权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 请求权限
                ActivityCompat
                        .requestPermissions(
                                this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                2);
            }

        } else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
        }
        new AppApplication.InitBarCodeTask().execute();

    }

    private void initview() {
        txtMobi.setText("admin");
        txtPassword.setText("123");
        String defautIp = ACache.get(AppApplication.getApplication()).getAsString("ip");
        if (defautIp == null) {
            ACache.get(AppApplication.getApplication()).put("ip", UIConstant.IP);
        }

        //把txtMobi转换为observable,返回TextViewTextObservable(view)FF
        final InitialValueObservable<CharSequence> obMobi = RxTextView.textChanges(txtMobi);
        final InitialValueObservable<CharSequence> obpassword = RxTextView.textChanges(txtPassword);

        //表单的验证
        Observable.combineLatest(obMobi, obpassword, new BiFunction<CharSequence, CharSequence, Boolean>() {

            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2) throws Exception {
                return isPhoneValid(charSequence.toString()) && isPasswordValid(charSequence2.toString());
            }
        }).subscribe(new Consumer<Boolean>() {

            @Override
            public void accept(Boolean aBoolean) throws Exception {
                RxView.enabled(btnLogin).accept(aBoolean);
                //点击事件可用
            }
        });

        //rxbinding 中的rxview点击事件,RxView.clicks(btnLogin)为被观察者，subscribe订阅，Consumer为观察者
        RxView.clicks(btnLogin).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                String username = txtMobi.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                mPresenter.login(username, password);
            }
        });

        rfid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ServerIpDialog serverIpDialog = new ServerIpDialog(LoginActivity.this);
                serverIpDialog.show();
                return false;
            }
        });

    }

    private boolean isPhoneValid(String phone) {
        return !TextUtils.isEmpty(phone);
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password);
    }

    @Override
    public void showLoading() {
        btnLogin.showLoading();
    }

    @Override
    public void showError(String msg) {
        btnLogin.showButtonText();
        Toast.makeText(LoginActivity.this, "登录失败：" + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissLoading() {
        btnLogin.showButtonText();
    }

    @Override
    public void checkError() {
        ToastUtil.toast("登录失败");
    }

    @Override
    public void loginSuccess(LoginBean bean) {
        ACache.get(AppApplication.getApplication()).put(ACacheUtils.USER_NAME, AppApplication.getGson().toJson(bean));

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    protected void onDestroy() {
        if (AppApplication.mReader != null) {
            AppApplication.mReader.free();
        }
        if (AppApplication.barcode2DWithSoft != null) {
            AppApplication.barcode2DWithSoft.stopScan();
            AppApplication.barcode2DWithSoft.close();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
