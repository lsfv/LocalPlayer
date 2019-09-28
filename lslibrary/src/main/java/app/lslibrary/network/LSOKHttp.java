package app.lslibrary.network;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import app.lslibrary.androidHelper.LSLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public abstract class LSOKHttp
{
    private static OkHttpClient client = initializeHttpClient();
    private static ReentrantLock mLock = new ReentrantLock();
    private static boolean isDebug = true;

    //单个全局变量不需要Singal模式，直接定义为static就ok.
    //测试发现，不需要加锁都是线程安全的，就算你在其他线程想得到它。也只有主线程才有资格初始化它一次。
    //不过需要编译器版本支持的东西，还是不规范，能用单例模式就单例模式吧.毕竟那个是语言规定死了的。
    private static OkHttpClient initializeHttpClient()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(6000, TimeUnit.MILLISECONDS)
                .readTimeout(9000, TimeUnit.MILLISECONDS)
                .writeTimeout(11000, TimeUnit.MILLISECONDS);
        if (isDebug)
        {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        return builder.build();
    }

    //enqueue,这个方法猜测应该是开了新线程处理网络。接受完数据回到ui线程，并线程安全得到数据，并回调高层方法。
    public static void get(String url, final Callback callbackHandler)
    {
        try
        {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(callbackHandler);
        } catch (Exception e)
        {
            LSLog.Log_Exception(e);
        }
    }

    //为了和调用方解耦，还是接受Map<String,String>这个参数。方便调用方以后更改为其他网络层。
    public static void post(String url, Map<String, String> parameter, final Callback callbackHandler)
    {
        FormBody.Builder formbody = new FormBody.Builder();
        for (Map.Entry<String, String> item : parameter.entrySet())
        {
            formbody.add(item.getKey(), item.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formbody.build())
                .build();
        client.newCall(request).enqueue(callbackHandler);
    }

    /**
     * it is a synctask. so you should not invoke it in ui thread.
     *
     * @param url             you want to get
     * @param callbackHandler call back
     */
    public static void getSync(String url, Callback callbackHandler)
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call callab = client.newCall(request);
        Response response = null;
        try
        {
            response = callab.execute();
            callbackHandler.onResponse(callab, response);
        } catch (Exception e)
        {
            IOException exception = new IOException(e.toString());
            callbackHandler.onFailure(callab, exception);
        } finally
        {
            response.close();//need close.bcz exception will be memory leaking.
        }
    }
}