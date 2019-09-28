package app.lslibrary.androidHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

public class LSNotification
{
    public static NotificationManager getManager(Context context)
    {
        return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static Notification getNotification(Context context, String title, @Nullable Integer progress, int icon, String content, Class<?> activityClass)
    {
        Notification.Builder builder=new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));

        if(progress!=null && progress>=0)
        {
            builder.setProgress(100, progress, false);
        }

        if(activityClass!=null)
        {
            Intent intent = new Intent(context, activityClass);
            PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
        }

        return builder.build();
    }
}