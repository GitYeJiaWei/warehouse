package com.ioter.warehouse.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ioter.warehouse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickActivity extends NewBaseActivity {

    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);
        ButterKnife.bind(this);

        setTitle("拣货");
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:

                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
