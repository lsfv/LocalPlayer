package app.lslibrary.io;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import app.lslibrary.androidHelper.LSLog;

public abstract class LSFileHelper
{
    public static void Write2File(String filePath, String content, boolean isappend)
    {
        try
        {
            File f = new File(filePath);
            FileOutputStream fop = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            writer.append(content);
            writer.close();
            fop.close();
        }
        catch (IOException e)
        {
            LSLog.Log_Exception(e);
        }
    }

    public static String ReadFromFile(String filePath)
    {
        String retString = null;
        try
        {
            File f = new File(filePath);
            FileInputStream finoutStream = new FileInputStream(f);
            InputStreamReader reader = new InputStreamReader(finoutStream, "UTF-8");

            StringBuffer sb = new StringBuffer();
            while (reader.ready())
            {
                sb.append((char) reader.read());
            }
            retString = sb.toString();

            reader.close();
            finoutStream.close();
        } catch (IOException e)
        {
            retString = null;
            LSLog.Log_Exception(e);
        }

        return retString;
    }


    public static String ReadFromResource(String filePath, Context ct)
    {
        InputStream is = ct.getClass().getClassLoader().getResourceAsStream(filePath);//android studio
        //InputStream in = ct.getResources().getAssets().open(fileName);
        if (is != null)
        {
            return readFromFile(is);
        } else
        {
            return null;
        }
    }

    //@linson no check exception
    public static Boolean CopyDataBaseFromResource(String dbname, Context ct, boolean overWrite)
    {
        //1.inputstream 2.outputstream 3.copy
        Boolean ret = false;
        InputStream myInput = ct.getClass().getClassLoader().getResourceAsStream("assets/" + dbname);//android studio

        File outFileName = ct.getDatabasePath(dbname);

        if (!outFileName.exists())
        {
            outFileName.getParentFile().mkdirs();
            try
            {
                OutputStream myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0)
                {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
                ret = true;
            } catch (Exception e)
            {
                ret = false;
                LSLog.Log_Exception(e);
            }
        }
        else if(outFileName.exists() && overWrite) {
            try
            {
                outFileName.deleteOnExit();
                OutputStream myOutput = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0)
                {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
                ret = true;
            } catch (Exception e)
            {
                ret = false;
                LSLog.Log_Exception(e);
            }
        }

        return ret;
    }



    private static String readFromFile(InputStream inputStream)
    {
        String retString = null;
        try
        {
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            StringBuffer sb = new StringBuffer();
            while (reader.ready())
            {
                sb.append((char) reader.read());
            }
            retString = sb.toString();

            reader.close();
        } catch (IOException e)
        {
            retString = null;
            LSLog.Log_Exception(e);
        }

        return retString;
    }


    //没有做任何测试.
    private Uri SetupImageURI(String parentPath, String filename, String mFileProviderAuthority, Context context)
    {
        Uri res=null;
        File theImage=new File(parentPath, filename);
        try
        {
            theImage.deleteOnExit();
            theImage.createNewFile();
        }
        catch (IOException e)
        {
            LSLog.Log_Exception(e);
        }
        if(Build.VERSION.SDK_INT>=24)
        {
            res=FileProvider.getUriForFile(context, mFileProviderAuthority, theImage);
        }
        else
        {
            res=Uri.fromFile(theImage);
        }

        return  res;
    }
}
