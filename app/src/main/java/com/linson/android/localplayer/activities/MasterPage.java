package com.linson.android.localplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linson.android.localplayer.R;
import com.linson.android.localplayer.appHelper;

import app.lslibrary.androidHelper.LSActivity;
import app.lslibrary.androidHelper.LSUI;

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
//!TODO 内存泄漏要注意静态变量和单例模式.单例对象在初始化后将在 JVM 的整个生命周期中存在（以静态变量的方式），如果单例对象持有外部的引用，那么这个外部对象在程序关闭之前都不能被回收。


//功能:母模板实现大框架功能。并提供public方法让fragment访问。
public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    private boolean loadMenu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);

        mMyControls=new MyControls();//cut it into 'onCreate'//1.控件绑定
        controlsEvent();
        setupDrawerMenu();//2.配置左侧滑动菜单
        CleanStackAndReplaceFragment(new ListIndex());//3.加载首页
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_page1:
            {
                CleanStackAndReplaceFragment(new ListIndex());
                mMyControls.mDrawerMainMenu.closeDrawer(Gravity.START);
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

    //region private function
    private void controlsEvent()
    {
        mMyControls.mBtnBack.setOnClickListener(this);
        mMyControls.mBtnPage1.setOnClickListener(this);
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

    private void CleanStackAndReplaceFragment(Fragment fragment)
    {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        appHelper.startPageNoBack(getSupportFragmentManager(), fragment);
    }
    //endregion

    //region 提供给fragment访问的功能
    public void changeMenuTitel(int index,String title)
    {
        mMyControls.mToolbar.getMenu().getItem(index).setTitle(title);
    }

    public void setupToolbarMenu(java.util.List<String> menus, android.support.v7.widget.Toolbar.OnMenuItemClickListener handler)
    {
        LSUI.setupToolbarMenu(mMyControls.mToolbar, menus, handler);
    }
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