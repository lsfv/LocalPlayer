package app.lslibrary.pattern;

import java.util.ArrayList;
import java.util.List;

public class LSObserver<T>
{
    public interface IObserverListener<T>
    {
        void onHappen(T p);
    }

    public List<IObserverListener> LISTENERS = new ArrayList<>();

    public void registerObserver(IObserverListener listener)
    {
        LISTENERS.add(listener);
    }

    public void unRegisterObserver(IObserverListener listener)
    {
        LISTENERS.remove(listener);
    }

    public void NoticeObsserver(T parameter)
    {
        for (IObserverListener item : LISTENERS)
        {
            if (item != null)
            {
                item.onHappen(parameter);
            }
        }
    }
}