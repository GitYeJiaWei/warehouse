package com.ioter.warehouse.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.ACache;
import com.ioter.warehouse.common.util.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Connect password dialog to enter reader password to connect with reader
 */
public class ServerIpDialog extends Dialog
{
    private final Activity activity;
    @BindView(R.id.btn_connect)
    Button connect;
    @BindView(R.id.btn_cancel)
    Button cancel;
    @BindView(R.id.ip_et)
    EditText ip_et;

    public ServerIpDialog(Activity activity)
    {
        super(activity);
        this.activity = activity;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_server_setup);
        ButterKnife.bind(this);

        ip_et.setHint(ACache.get(AppApplication.getApplication()).getAsString("ip"));
    }

    @OnClick({R.id.btn_connect, R.id.btn_cancel})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_connect:
                dismiss();
                String ip = ip_et.getText().toString().trim();
                if (ip != null && ip.length() > 0)
                {
                    if (isboolIp(ip))
                    {
                        ACache.get(AppApplication.getApplication()).put("ip", ip);
                        ToastUtil.toast("设置IP地址成功");
                    } else
                    {
                        ToastUtil.toast("不是合法IP地址");
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 判断是否为合法IP * @return the ip
     */
    private boolean isboolIp(String ipAddress)
    {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
