package com.ioter.warehouse.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ioter.warehouse.R;
import com.ioter.warehouse.common.util.ACacheUtils;
import com.ioter.warehouse.common.util.ToastUtil;
import com.ioter.warehouse.ui.adapter.DrawerListAdapter;
import com.ioter.warehouse.ui.adapter.DrawerListContent;
import com.ioter.warehouse.ui.dialog.BaseDialog;
import com.ioter.warehouse.ui.dialog.InParamSetupDialog;
import com.ioter.warehouse.ui.fragment.HomeFragment;
import com.ioter.warehouse.ui.fragment.InBaseFragment;
import com.ioter.warehouse.ui.fragment.InCheckFragment;
import com.ioter.warehouse.ui.fragment.InSingleRfidFragment;
import com.rscja.deviceapi.RFIDWithUHF;
import com.zebra.adc.decoder.Barcode2DWithSoft;


/**
 *
 */
public class MainActivity extends AppCompatActivity{

    public RFIDWithUHF mReader; //RFID扫描
    public Barcode2DWithSoft barcode2DWithSoft = null; //扫条码

    protected static final String TAG_CONTENT_FRAGMENT = "ContentFragment";
    protected Menu menu;

    private String[] mOptionTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View headerView;
    private ImageView mImg_avatar;
    private TextView mTxt_username;
    private InParamSetupDialog dialog;

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

        //initUHF();
        //initBarCode();
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


  /*  private int getActionBarIcon()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
        return -1;
    }*/

    //收货
    public void isReceiving(View view)
    {
        Fragment fragment = InSingleRfidFragment.newInstance();
        //选择fragment
        replaceFragment(3, fragment);
    }

    //上架
    public void isGrounding(View view)
    {

    }

    //拣货
    public void isPicking(View view)
    {

    }

    //移仓
    public void isMoving(View view)
    {

    }

    //查询
    public void isFinding(View view)
    {

    }

    //库存盘点
    public void isChecking(View view)
    {
        Fragment fragment = InCheckFragment.newInstance();
        //选择fragment
        replaceFragment(8, fragment);
    }


    //设置
    public void isSetting(View view)
    {
        startActivity(new Intent(MainActivity.this,SettingActivity.class));
    }

    //初始化RFID扫描
    public void initUHF()
    {
        try
        {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex)
        {
            ToastUtil.toast(ex.getMessage());
            return;
        }

        if (mReader != null)
        {
            new InitTask().execute();
        }
    }

    private int cycleCount = 3;//循环3次初始化

    public class InitTask extends AsyncTask<String, Integer, Boolean>
    {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params)
        {
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result)
            {
                Toast.makeText(MainActivity.this, "init uhf fail,reset ...",
                        Toast.LENGTH_SHORT).show();
                if(cycleCount > 0)
                {
                    cycleCount--;
                    if (mReader != null)
                    {
                        mReader.free();
                    }
                    initUHF();
                }
            }else
            {
                Toast.makeText(MainActivity.this, "init uhf success",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            mypDialog = new ProgressDialog(MainActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init uhf...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    //初始化二维码扫描头
    public void initBarCode()
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
        new InitBarCodeTask().execute();
    }

    public class InitBarCodeTask extends AsyncTask<String, Integer, Boolean>
    {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params)
        {

            if (barcode2DWithSoft == null)
            {
                barcode2DWithSoft = Barcode2DWithSoft.getInstance();
            }
            boolean reuslt = false;
            if (barcode2DWithSoft != null)
            {
                reuslt = barcode2DWithSoft.open(MainActivity.this);
            }
            return reuslt;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (result)
            {
                barcode2DWithSoft.setParameter(324, 1);
                barcode2DWithSoft.setParameter(300, 0); // Snapshot Aiming
                barcode2DWithSoft.setParameter(361, 0); // Image Capture Illumination

                // interleaved 2 of 5
                barcode2DWithSoft.setParameter(6, 1);
                barcode2DWithSoft.setParameter(22, 0);
                barcode2DWithSoft.setParameter(23, 55);

                Toast.makeText(MainActivity.this, "init barCode Success", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(MainActivity.this, "init barCode fail", Toast.LENGTH_SHORT).show();
            }
            mypDialog.cancel();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            mypDialog = new ProgressDialog(MainActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init barCode...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }


    public Barcode2DWithSoft.ScanCallback
            ScanBack = new Barcode2DWithSoft.ScanCallback()
    {
        @Override
        public void onScanComplete(int i, int length, byte[] bytes)
        {
            if (length < 1)
            {
                if (length == -1)
                {
                    //data1.setText("Scan cancel");
                } else if (length == 0)
                {
                    //data1.setText("Scan TimeOut");
                } else
                {
                    //Log.i(TAG,"Scan fail");
                }
            } else
            {
               /* String barCode = new String(bytes, 0, length);
                if (barCode != null && barCode.length() > 0)
                {
                    SoundManage.PlaySound(MainActivity.this, SoundManage.SoundType.SUCCESS);
                    //收货弹出框
                    if (dialog != null && dialog.isShowing())
                    {
                        dialog.setBarCode(barCode);
                    }
                    //sku转epc
                    final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
                    if (fragment != null)
                    {
                        if ( fragment instanceof InSingleRfidFragment )
                        {
                            DebugUtil.printe("barCode",barCode);
                            ((BaseFragment) fragment).setBarCode(barCode);
                        }
                    }

                }*/
            }
        }
    };



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
        if (keyCode == 139)
        {
            if (fragment != null && (fragment instanceof InSingleRfidFragment ))
            {
                if (event.getRepeatCount() == 0)
                {
                    ScanBarcode();
                    return true;
                }
            }
        }else if(keyCode == 280)
        {

            if (fragment instanceof InSingleRfidFragment || fragment instanceof InCheckFragment)
            {
                if (event.getRepeatCount() == 0)
                {
                    ((InBaseFragment) fragment).myOnKeyDwon();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
        if (keyCode == 139)
        {
            if (fragment != null && ( fragment instanceof InSingleRfidFragment ))
            {
                if (event.getRepeatCount() == 0)
                {
                    barcode2DWithSoft.stopScan();
                    return true;
                }
            }
        }else if(keyCode == 280)
        {

            if ( fragment instanceof InSingleRfidFragment ||  fragment instanceof InCheckFragment )
            {
                if (event.getRepeatCount() == 0)
                {
                    ((InBaseFragment) fragment).myOnKeyDwon();
                }
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    //扫条码
    private void ScanBarcode()
    {
        if (barcode2DWithSoft != null)
        {
            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(ScanBack);
        }
    }

    @Override
    protected void onDestroy()
    {
        if (mReader != null)
        {
            mReader.free();
        }
        if (barcode2DWithSoft != null)
        {
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
    }


    //获取后退键
    @Override
    public void onBackPressed()
    {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT_FRAGMENT);
        if (fragment != null)
        {
            if (fragment instanceof InCheckFragment)
            {
                getPressDialog("确定退出盘点？");
            }else if (fragment instanceof InSingleRfidFragment)
            {
                final BaseDialog baseDialog = new BaseDialog(this, 1);
                baseDialog.setHintTvValue("确定退出收货？");
                baseDialog.setConfrimBtnOnclick(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        baseDialog.dismiss();
                        if (fragment instanceof InSingleRfidFragment)
                        {
                            ((InSingleRfidFragment) fragment).stopInventory();
                        }
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
            } else if (fragment instanceof HomeFragment)
            {
                final BaseDialog baseDialog = new BaseDialog(this, 1);
                baseDialog.setHintTvValue("确定退出应用？");
                baseDialog.setConfrimBtnOnclick(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        baseDialog.dismiss();
                        press();
                    }
                });
            } else
            {
                press();
            }
        } else
        {
            press();
        }
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
}



