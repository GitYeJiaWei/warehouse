package com.ioter.warehouse.ui.activity;

import android.os.Bundle;

import com.ioter.warehouse.R;

public class CheckActivity extends NewBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        setTitle("库存盘点");
    }
}
