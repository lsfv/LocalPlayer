package com.linson.android.localplayer;

import android.content.Context;
import android.content.Intent;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;

import app.bll.List;
import app.lslibrary.androidHelper.LSLog;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;


//单元测试，很重要的部分。有点像硬件的自检。是保证系统健壮的必要部分。
//1.数据库连接。2.重要函数的验证。3。基础功能的ui检测。
//1.连接数据库，并修改，再读再验证。
//添加一个列表，再删除。
//播放一首歌

//最简单的单元测试完成。哈哈。可以修改的时候，安全测试了。
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    private static final String BASIC_SAMPLE_PACKAGE = "com.linson.android.localplayer";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;


    @Before
    public void startMainActivityFromHomeScreen()
    {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }



    //song list test.
    @Test
    public void accessDB()
    {
        mDevice.waitForIdle(3000);
        UiObject rv=mDevice.findObject(new UiSelector().descriptionContains("index_rv_list"));

        assertTrue(rv.exists());//必须做这一步，否则为空，后面的语句会进入异常判断，而跳过了单元测试判断。

        //数据库是否ok。
        int currentsize=app.bll.List.getAlllList().size();
        try
        {
            assertEquals(rv.getChildCount(), currentsize);
        }
        catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }

        //添加测试
        UiObject btn_add=mDevice.findObject(new UiSelector().text("添加列表"));
        assertTrue(btn_add.exists());

        try
        {
            btn_add.click();
        } catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }

        mDevice.waitForIdle(3000);

        UiObject input_list= mDevice.findObject(new UiSelector().descriptionContains("listnameinput"));
        assertTrue(input_list.exists());

        try
        {
            input_list.setText("好的a");
        } catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }

        UiObject input_ok= mDevice.findObject(new UiSelector().text("确定"));
        assertTrue(input_ok.exists());

        try
        {
            input_ok.click();
        }
        catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }

        int newcurrentsize=app.bll.List.getAlllList().size();
        assertEquals(newcurrentsize, currentsize+1);


        UiObject rv2=mDevice.findObject(new UiSelector().descriptionContains("index_rv_list"));
        assertTrue(rv2.exists());//必须做这一步，否则为空，后面的语句会进入异常判断，而跳过了单元测试判断。

        try
        {
            UiObject item= rv2.getChild(new UiSelector().index(rv2.getChildCount()-1));
            item.swipeLeft(5);

            UiObject item_delete= item.getChild(new UiSelector().text("Delete"));
            item_delete.click();
        }
        catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }


        int newcurrentsize2=app.bll.List.getAlllList().size();
        assertEquals(newcurrentsize2, currentsize);

    }


    @Test
    public void player()
    {
        UiObject rv2=mDevice.findObject(new UiSelector().descriptionContains("index_rv_list"));
        assertTrue(rv2.exists());//必须做这一步，否则为空，后面的语句会进入异常判断，而跳过了单元测试判断。

        try
        {
            UiObject item= rv2.getChild(new UiSelector().index(0));
            item.click();
            UiObject rv_song=mDevice.findObject(new UiSelector().descriptionContains("rv_songlist"));
            assertTrue(rv_song.exists());
            UiObject song_item= rv_song.getChild(new UiSelector().index(1));
            assertTrue(song_item.exists());
            song_item.click();
            song_item.click();
            mDevice.wait(Until.findObjects(By.textStartsWith("noway")),9000);
        }
        catch (UiObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}