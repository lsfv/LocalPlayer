package app.lslibrary.androidHelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import app.lslibrary.R;

public abstract class LSActivity
{
    public static void addFragment(FragmentManager fragmentManager, boolean backStack, int holderid, Fragment fragment)
    {
        dealFragment(true, fragmentManager, backStack, holderid, fragment);
    }

    public static void replaceFragment(FragmentManager fragmentManager, boolean backStack, int holderid, Fragment fragment)
    {
        dealFragment(false, fragmentManager, backStack, holderid, fragment);
    }

    private static void dealFragment(boolean addorreplace,FragmentManager fragmentManager, boolean backStack, int holderid, Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(addorreplace)
        {
            fragmentTransaction.add(holderid, fragment);
        }
        else
        {
            fragmentTransaction.replace(holderid, fragment);
        }
        if(backStack)
        {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}