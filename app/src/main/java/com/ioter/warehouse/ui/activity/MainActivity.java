package com.ioter.warehouse.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ioter.warehouse.R;
import com.ioter.warehouse.common.ActivityCollecter;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.ui.adapter.DrawerListAdapter;
import com.ioter.warehouse.ui.adapter.DrawerListContent;
import com.ioter.warehouse.ui.dialog.BaseDialog;
import com.ioter.warehouse.ui.fragment.HomeFragment;


/**
 *
 */
public class MainActivity extends NewBaseActivity{

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
        initview();
        if (savedInstanceState == null)
        {
            selectItem(0);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollecter.finishAll();
    }
}
