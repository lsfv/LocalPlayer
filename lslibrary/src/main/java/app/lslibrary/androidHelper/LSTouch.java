package app.lslibrary.androidHelper;

import android.view.MotionEvent;

public abstract class LSTouch
{
    public enum scrollDirection
    {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        stop,
        init
    }
    public static scrollDirection getscrollDirection(MotionEvent firstEvent, MotionEvent secondEvent)
    {
//        LSLog.Log_INFO(firstEvent.toString());
//        LSLog.Log_INFO(secondEvent.toString());
//        LSLog.Log_INFO((secondEvent.getY()>firstEvent.getY())+"."+(secondEvent.getX()<firstEvent.getX())+".");
//        LSLog.Log_INFO((Math.abs(secondEvent.getY()-firstEvent.getY())>Math.abs(secondEvent.getX()-firstEvent.getX()))+"."+(Math.abs(secondEvent.getY()-firstEvent.getY())<Math.abs(secondEvent.getX()-firstEvent.getX())));
        if(secondEvent.getY()>firstEvent.getY() && Math.abs(secondEvent.getY()-firstEvent.getY())>Math.abs(secondEvent.getX()-firstEvent.getX()))//down
        {
            return scrollDirection.DOWN;
        }
        else if(secondEvent.getY()<firstEvent.getY() && Math.abs(secondEvent.getY()-firstEvent.getY())>Math.abs(secondEvent.getX()-firstEvent.getX()))//up
        {
            return scrollDirection.UP;
        }
        else if(secondEvent.getX()<firstEvent.getX() && Math.abs(secondEvent.getY()-firstEvent.getY())<Math.abs(secondEvent.getX()-firstEvent.getX()))//left
        {
            return scrollDirection.LEFT;
        }
        else if(secondEvent.getX()>firstEvent.getX() && Math.abs(secondEvent.getY()-firstEvent.getY())<Math.abs(secondEvent.getX()-firstEvent.getX()))//right
        {
            return scrollDirection.RIGHT;
        }
        else
        {
            return scrollDirection.stop;
        }
    }
}