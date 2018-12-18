package com.ioter.warehouse.ui.activity;

import android.os.Bundle;

import com.ioter.warehouse.R;

public class MoveActivity extends NewBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        setTitle("库存移动");
    }
}
