package com.linson.android.localplayer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.bll.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext()
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.linson.android.localplayer", appContext.getPackageName());
    }

    @Test
    public void testdb_add()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        app.bll.List list_bll=new List(appContext);

        app.model.List temp=new app.model.List("test314","info","","ps");
        list_bll.add(temp);

        java.util.List<app.model.List> res = list_bll.getModelList("");
        assertEquals(res.get(res.size()-1).L_name, "test314");
    }
}