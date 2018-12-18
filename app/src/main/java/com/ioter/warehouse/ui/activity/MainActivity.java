package com.ioter.warehouse.ui.activity;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.AppApplication;
import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.ui.adapter.DrawerListAdapter;
import com.ioter.warehouse.ui.adapter.DrawerListContent;
import com.ioter.warehouse.ui.dialog.BaseDialog;
import com.ioter.warehouse.ui.fragment.HomeFragment;


/**
 *
 */
public class MainActivity extends AppCompatActivity{

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";
    protected Menu menu;

    private String[] mOptionTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View headerView;
    private ImageView mImg_avatar;
    private TextView mTxt_username;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
        initview();
        if (savedInstanceState == null)
        {
            selectItem(0);
        }
    }

    //初始化二维码扫描头
    public void init()
    {
        if (Build.VERSION.SDK_INT > 21)
        {

            //扫条码 需要相机对应用开启相机和存储权限；
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else
            {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
            //读写内存权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // 请求权限
                ActivityCompat
                        .requestPermissions(
                                this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                2);
            }

        } else
        {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
        }
        new AppApplication.InitBarCodeTask().execute();
    }

    private void initview(){
        mOptionTitles = getResources().getStringArray(R.array.options_array);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mDrawerList =  findViewById(R.id.left_drawer);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_header,mDrawerList,false);
        mDrawerList.addHeaderView(headerView);
        mImg_avatar =  headerView.findViewById(R.id.img_avatar);
        mTxt_username =  headerView.findViewById(R.id.txt_username);
        mTxt_username.setText(ACacheUtils.getUserName());

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new DrawerListAdapter(this,R.layout.drawer_list_item, DrawerListContent.ITEMS));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * The click listener for ListView in the navigation drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            if (position == 0)//headView click
            {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else
            {
                selectItem(position);
            }
        }
    }

    private void selectItem(int position)
    {
        // update the no_items content by replacing fragments
        Fragment fragment = null;

        switch (position)
        {
            case 0:
                fragment = HomeFragment.newInstance();
                break;
            case 1:
                mDrawerLayout.closeDrawer(mDrawerList);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return;

            case 2:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return;
        }
       replaceFragment(position, fragment);
    }

    public void replaceFragment(int position, Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0)
        {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG_CONTENT_FRAGMENT).commit();
        } else
        {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, TAG_CONTENT_FRAGMENT).addToBackStack(null).commit();
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mOptionTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //收货
    public void isReceiving(View view)
    {
        startActivity(new Intent(MainActivity.this,ReceiveActivity.class));
    }

    //上架
    public void isGrounding(View view)
    {
        startActivity(new Intent(MainActivity.this,GroundActivity.class));
    }

    //拣货
    public void isPicking(View view)
    {
        startActivity(new Intent(MainActivity.this,PickActivity.class));
    }

    //库存移动
    public void isMoving(View view)
    {
        startActivity(new Intent(MainActivity.this,MoveActivity.class));
    }

    //库存查询
    public void isFinding(View view)
    {
        startActivity(new Intent(MainActivity.this,FindActivity.class));
    }

    //库存盘点
    public void isChecking(View view)
    {
        startActivity(new Intent(MainActivity.this,CheckActivity.class));
    }


    //系统设置
    public void isSetting(View view)
    {
        startActivity(new Intent(MainActivity.this,SettingActivity.class));
    }


    public void press()
    {
        //update the selected item in the drawer and the title
        mDrawerList.setItemChecked(0, true);
        setTitle(mOptionTitles[0]);
        try
        {
            super.onBackPressed();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getPressDialog(String content)
    {
        final BaseDialog baseDialog = new BaseDialog(this, 1);
        baseDialog.setHintTvValue(content);
        baseDialog.setConfrimBtnOnclick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                baseDialog.dismiss();
                press();
            }
        });
        baseDialog.setCancelBtnOnclick(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                baseDialog.dismiss();
            }
        });
    }

    protected void onDestroy() {
        if (AppApplication.mReader != null)
        {
            AppApplication.mReader.free();
        }
        if (AppApplication.barcode2DWithSoft != null)
        {
            AppApplication.barcode2DWithSoft.stopScan();
            AppApplication.barcode2DWithSoft.close();
        }
        super.onDestroy();
    }
}



