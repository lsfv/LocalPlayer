package com.linson.android.localplayer;

import android.support.annotation.NonNull;

import org.junit.Test;

import app.model.Song;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    public static final app.model.Song abc = new Song();
    @Test
    public void addition_isCorrect()
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void tt()
    {
        abc.S_musicName="hi";
    }

    public void testnull(@NonNull Integer abc)
    {
        int a=abc/2;
    }
}