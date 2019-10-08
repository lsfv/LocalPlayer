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

//!todo! 1，还有一个不是很完善的地方：if(fragment instanceof ISetupMaster)。 没有强制的要求接口。
public class MasterPage extends AppCompatActivity implements View.OnClickListener
{
    //region 母模板 自己功能实现的代码块。
    private DrawerLayout mDrawerMainMenu;
    private Toolbar mToolbar;
    private ConstraintLayout mMainFragment;
    private Button mBtnPage1;
    private Button mBtnPage2;


    private boolean loadMenu=false;


    //region  findcontrols and bind click event.
    private void findControls()
    {   //findControls
        mDrawerMainMenu = (DrawerLayout) findViewById(R.id.drawerMainMenu);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMainFragment = (ConstraintLayout) findViewById(R.id.mainFragment);
        mBtnPage1 = (Button) findViewById(R.id.btn_page1);
        mBtnPage2 = (Button) findViewById(R.id.btn_page2);

        //set event handler
        mBtnPage1.setOnClickListener(this);
        mBtnPage2.setOnClickListener(this);
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
            case R.id.btn_page2:
            {
                startPage(new ListDetail());
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
        setupMenu();//2.配置菜单
        startPage(new ListIndex());//3.加载首页
    }


    private void setupMenu()
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