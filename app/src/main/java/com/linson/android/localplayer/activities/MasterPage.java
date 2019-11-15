package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linson.android.localplayer.R;

import app.lslibrary.androidHelper.LSActivity;

//!todo 3 codereview！ 4.panel 5.autoupdate.
//!todo viewpager 1，参数页。2分部门页都有后续页。
//!todo 2.code review 3.allsong.4.panel 5.autoupdate.
//!todo 无法触发服务sub代理的释放.android 一般也是不完全关闭app的。所以我这里是保证服务停止播放，最多释放播放器而已。
//!todo 还是需要自带的常用所有控件都过一遍。是否需要建立一个歌词服务器?
//!todo 1，还有一个不是很完善的地方：if(fragment instanceof ISetupMaster)。 没有强制的要求接口。
//!todo 更新歌单，可能需要一个更低耗的方法。
//!todo 左侧菜单栏目没有清空回退的功能。
//!todo savedInstanceState 实际工程使用范例.
//!todo 还是需要一个模板啊。比如adapter 的大致样子都是差不多的。
//!todo 界面更新的逻辑，根据编码的原则和2个方案的有缺点，决定还是server主动的才广播。否则还是用耦合度高的一个动作更新2个子界面的方式处理。
//!todo 需要模板生成器。
//!todo getMenuTitle 并没有保证会加入所有菜单。
//!todo public ListDetail(int a) 什么时候fragment需要从建立开始恢复？ 导致得到参数必须是通过argumentbundle。
//!TODO 内存泄漏，那就不要使用单例模式了?

public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    public static final String FIXMENUTITLENAME="title";
    private boolean loadMenu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);

        mMyControls=new MyControls();//cut it into 'onCreate'//1.控件绑定
        mMyControls.mBtnBack.setOnClickListener(this);
        mMyControls.mBtnPage1.setOnClickListener(this);
        setupDrawerMenu();//2.配置左侧滑动菜单
        startPage(new ListIndex());//3.加载首页
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_page1:
            {
                startPage(new ListIndex());
                break;
            }
            case R.id.btn_back:
            {
                if(getSupportFragmentManager().getBackStackEntryCount()>0)
                {
                    getSupportFragmentManager().popBackStack();
                }
            }
            default: { break; }
        }
    }


    private void setupDrawerMenu()
    {
        mMyControls.mToolbar.setNavigationIcon(R.drawable.list);
        mMyControls.mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mMyControls.mDrawerMainMenu.openDrawer(Gravity.START);
            }
        });
    }

    private void startPage(Fragment fragment)
    {
        LSActivity.replaceFragment(getSupportFragmentManager(), false, R.id.mainFragment, fragment);
        mMyControls.mDrawerMainMenu.closeDrawer(Gravity.START);
    }



    //region 母模板public出去的方法，提供给fragment使用。统一管理.
    @SuppressLint("AlwaysShowAction")
    public void setupToolbarMenu(java.util.List<String> menus, android.support.v7.widget.Toolbar.OnMenuItemClickListener handler)
    {
        mMyControls.mToolbar.setOnMenuItemClickListener(handler);
        mMyControls.mToolbar.getMenu().clear();
        for(int i=0;i<menus.size();i++)
        {
            MenuItem tempitem= mMyControls.mToolbar.getMenu().add(menus.get(i));
            Intent tempIntent=new Intent();
            tempIntent.putExtra(FIXMENUTITLENAME, menus.get(i));
            tempitem.setIntent(tempIntent);
            tempitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    public void changeMenuTitel(int index,String title)
    {
        mMyControls.mToolbar.getMenu().getItem(1).setTitle(title);
    }
    //endregion

    //region 母模板需要每个页面实现的功能接口。

    //endregion

    //region The class of FindControls
        private MyControls mMyControls=null;
        public class MyControls
        {
            private DrawerLayout mDrawerMainMenu;
            private Toolbar mToolbar;
            private Button mBtnBack;
            private ConstraintLayout mMainFragment;
            private Button mBtnPage1;

            public MyControls()
            {
                mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
                mToolbar = (Toolbar) findViewById(R.id.toolbar);
                mBtnBack = (Button) findViewById(R.id.btn_back);
                mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
                mBtnPage1 = (Button) findViewById(R.id.btn_page1);
            }
        }
    //endregion
}