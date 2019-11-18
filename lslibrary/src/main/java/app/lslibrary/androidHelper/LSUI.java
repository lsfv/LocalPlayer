package app.lslibrary.androidHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public abstract class LSUI
{
    public enum Scroll_Type
    {
        contentIsLeft,
        contentIsRight,
        contentIsTop,
        contentIsBottom,
        free,
        fix,
    }

    public static Scroll_Type checkHScrollType(RecyclerView recyclerView)
    {
        final int hoffset = recyclerView.computeHorizontalScrollOffset();
        final int hMaxoffset = recyclerView.computeHorizontalScrollRange() - recyclerView.computeHorizontalScrollExtent();

        if (hMaxoffset == 0)
        {
            return Scroll_Type.fix;
        }
        if(hoffset==0)
        {
            return Scroll_Type.contentIsLeft;
        }
        else if(hoffset>=hMaxoffset)
        {
            return Scroll_Type.contentIsRight;
        }
        return Scroll_Type.free;
    }


    public static Scroll_Type checkVScrollType(RecyclerView recyclerView)
    {
        final int voffset = recyclerView.computeVerticalScrollOffset();
        final int vMaxoffset = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent();

        if (vMaxoffset == 0)
        {
            return Scroll_Type.fix;
        }

        if(voffset==0)
        {
            return Scroll_Type.contentIsTop;
        }
        else if(voffset==vMaxoffset)
        {
            return Scroll_Type.contentIsBottom;
        }
        return Scroll_Type.free;
    }


    //配置一个菜单，每个菜单有一个显示字符和一个内部参数字符。都是同一个值。目的就是显示字符修改后。还可以通过内部参数字符找到原菜单。
    @SuppressLint("AlwaysShowAction")
    private static String TOOLBARITEMVARIABLENAME="titlename";
    public static void setupToolbarMenu(Toolbar toolbar, java.util.List<String> menus, android.support.v7.widget.Toolbar.OnMenuItemClickListener handler)
    {
        toolbar.setOnMenuItemClickListener(handler);
        toolbar.getMenu().clear();
        if(menus!=null)
        {
            for (int i = 0; i < menus.size(); i++)
            {
                MenuItem tempitem = toolbar.getMenu().add(menus.get(i));
                Intent tempIntent = new Intent();
                tempIntent.putExtra(TOOLBARITEMVARIABLENAME, menus.get(i));
                tempitem.setIntent(tempIntent);
                tempitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
    }

    public static String getToolbarItemKeyID(MenuItem item)
    {
        return item.getIntent().getStringExtra(TOOLBARITEMVARIABLENAME);
    }
}