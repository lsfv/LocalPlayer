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

//!todo 1.db   1.5.uitest.  2.app media.3 pageview 4.panel 5.autoupdate.
//!todo 还是需要自带的常用所有控件都过一遍。是否需要建立一个歌词服务器?
//!todo 1，还有一个不是很完善的地方：if(fragment instanceof ISetupMaster)。 没有强制的要求接口。
//!todo 更新歌单，可能需要一个更低耗的方法。
//!todo 左侧菜单栏目没有清空回退的功能。
//!todo ,返回到首页，有时候No adapter attached; skipping layout
//!todo savedInstanceState 实际工程使用范例.
//!todo 还是需要一个模板啊。比如adapter 的大致样子都是差不多的。
//!todo 界面更新的逻辑，根据编码的原则和2个方案的有缺点，决定还是server主动的才广播。否则还是用耦合度高的一个动作更新2个子界面的方式处理。
//!todo 如何查看警告和设置浸膏的级别。
//!todo 建立了aidl对象后实现后，无法停止service。虽然释放了播放器。不过对用户来说，已经停止了播放器，也算停止了服务。之后再看下。
//!todo 需要模板生成器。
//!todo getMenuTitle 并没有保证会加入所有菜单。
//!todo public ListDetail(int a) 什么时候fragment需要从建立开始恢复？ 导致得到参数必须是通过argumentbundle。

public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    //region 母模板 自己功能实现的代码块。
    private DrawerLayout mDrawerMainMenu;
    private Toolbar mToolbar;
    private ConstraintLayout mMainFragment;
    private Button mBtnPage1;
    private Button mbtn_back;
    private boolean loadMenu=false;

    public static final String FIXMENUTITLENAME="title";

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
        mBtnPage1 = (Button) findViewById(R.id.btn_page1);
        mbtn_back=(Button)findViewById(R.id.btn_back);

        //set event handler
        mBtnPage1.setOnClickListener(this);
        mbtn_back.setOnClickListener(this);
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
    //endregion

    //region other member variable
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_page);
        findControls();//1.控件绑定
        setupDrawerMenu();//2.配置左侧滑动菜单
        startPage(new ListIndex());//3.加载首页
    }


    private void setupDrawerMenu()
    {
        mToolbar.setNavigationIcon(R.drawable.list);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerMainMenu.openDrawer(Gravity.START);
            }
        });
    }

    private void startPage(Fragment fragment)
    {
        LSActivity.replaceFragment(getSupportFragmentManager(), false, R.id.mainFragment, fragment);
        mDrawerMainMenu.closeDrawer(Gravity.START);
    }


    //endregion


    //region 母模板public出去的方法，提供给fragment使用。统一管理.
    @SuppressLint("AlwaysShowAction")
    public void setupToolbarMenu(java.util.List<String> menus, android.support.v7.widget.Toolbar.OnMenuItemClickListener handler)
    {
        mToolbar.setOnMenuItemClickListener(handler);
        mToolbar.getMenu().clear();
        for(int i=0;i<menus.size();i++)
        {
            MenuItem tempitem= mToolbar.getMenu().add(menus.get(i));
            Intent tempIntent=new Intent();
            tempIntent.putExtra(FIXMENUTITLENAME, menus.get(i));
            tempitem.setIntent(tempIntent);
            tempitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    public void changeMenuTitel(int index,String title)
    {
        mToolbar.getMenu().getItem(1).setTitle(title);
    }
    //endregion

    //region 母模板需要每个页面实现的功能接口。
    public interface IFragmentForMaster
    {

    }
    //endregion
}