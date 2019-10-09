package com.linson.android.localplayer.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linson.android.localplayer.R;

import java.util.List;

import app.lslibrary.androidHelper.LSActivity;
import app.lslibrary.androidHelper.LSLog;

//!todo 1，还有一个不是很完善的地方：if(fragment instanceof ISetupMaster)。 没有强制的要求接口。
//!todo 逻辑层的分类，按表划分，会导致扩表操作的方法，无法定位放置位置。 如果按模块划分，如页面，页面太多，或小页面又会导致逻辑类太多。这个。
//!todo savedInstanceState 实际工程使用范例.
//!todo 1.建立界面 .2 建立初始化函数 3.填充数据recycleview. 4.处理编辑清单功能。  ！！！！清单id出现的不一样。
//!todo 还是需要一个模板啊。比如adapter 的大致样子都是差不多的。
public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    //region 母模板 自己功能实现的代码块。
    private DrawerLayout mDrawerMainMenu;
    private Toolbar mToolbar;
    private ConstraintLayout mMainFragment;
    private Button mBtnPage1;

    private boolean loadMenu=false;

    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
        mBtnPage1 = (Button) findViewById(R.id.btn_page1);

        //set event handler
        mBtnPage1.setOnClickListener(this);
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
                mDrawerMainMenu.openDrawer(Gravity.LEFT);
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                if(getSupportFragmentManager().findFragmentById(R.id.mainFragment) instanceof IFragmentForMaster)
                {
                    IFragmentForMaster fragment=(IFragmentForMaster) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
                    return fragment.onMenuItemClick(menuItem);
                }
                else
                {
                    return false;
                }
            }
        });
    }

    //无法和回退配合。
    private void startPage(Fragment fragment)
    {
        if(fragment instanceof IFragmentForMaster)
        {
            mToolbar.getMenu().clear();

            List<String> menus=((IFragmentForMaster) fragment).getMenuTitle();
            for(int i=0;i<menus.size();i++)
            {
                mToolbar.getMenu().add(menus.get(i));
                mToolbar.getMenu().getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        LSActivity.replaceFragment(getSupportFragmentManager(), false, R.id.mainFragment, fragment);

        mDrawerMainMenu.closeDrawer(Gravity.LEFT);
    }


    //endregion

    //region 母模板public出去的方法，提供给fragment使用。
    public void startPageWithBack(Fragment fragment)
    {
        if(fragment instanceof IFragmentForMaster)
        {
            mToolbar.getMenu().clear();

            List<String> menus=((IFragmentForMaster) fragment).getMenuTitle();
            for(int i=0;i<menus.size();i++)
            {
                mToolbar.getMenu().add(menus.get(i));
                mToolbar.getMenu().getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }

        LSActivity.replaceFragment(getSupportFragmentManager(), true, R.id.mainFragment, fragment);
    }
    //endregion

    //region 母模板需要每个页面实现的功能接口。
    public interface IFragmentForMaster
    {
        List<String> getMenuTitle();
        boolean onMenuItemClick(MenuItem menuItem);
    }
    //endregion
}