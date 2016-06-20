package um.feri.mihael.wi_finder;

import android.app.Application;
import android.app.ExpandableListActivity;
import android.os.Environment;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Mihael on 30. 05. 2016.
 */
public class ApplicationMy extends Application{
    private DataAll all = new DataAll();
    private GoogleSignInResult result;

    private static final String DATA_DIR = "hotspotsmapa";
    private static final String DATA_FILE = "data.json";


    public DataAll getAll()
    {
        return all;
    }

    public void setAll(DataAll all)
    {
        this.all = all;
    }

    public boolean loadData()
    {
        DataAll temp = loadData(DATA_FILE);

        if(temp != null)
        {
            all = temp;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean saveData()
    {
        return saveData(all, DATA_FILE);
    }

    private boolean saveData(DataAll dat, String fileName)
    {
        if(isExternalStorageWritable())
        {
            try
            {
                File file = new File(this.getExternalFilesDir(DATA_DIR), "" + fileName);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                PrintWriter printWriter = new PrintWriter(file);

                String saveString = gson.toJson(dat);
                printWriter.println(saveString);
                printWriter.close();

                return true;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.out.println("Error saving: File not found!");
            }
        }
        else
        {
            System.out.println(dat.getClass().getCanonicalName() + " NOT Writable");
        }

        return false;
    }

    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();

        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private DataAll loadData(String fileName)
    {
        if(isExternalStorageReadable())
        {
            try
            {
                File file = new File(this.getExternalFilesDir(DATA_DIR), "" + fileName);
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream inStream = new DataInputStream(fstream);
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream));
                StringBuffer strBuffer = new StringBuffer();
                String currLine;

                while((currLine = buffReader.readLine()) != null)
                {
                    strBuffer.append(currLine).append('\n');
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                DataAll readData = gson.fromJson(strBuffer.toString(), DataAll.class);

                return readData;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public GoogleSignInResult getSignInResult()
    {
        return result;
    }

    public boolean isUserLoggedIn()
    {
        if(result != null)
        {
            return result.isSuccess();
        }

        return false;
    }

    public boolean signInAvaliable()
    {
        return (result != null);
    }

    public void setSignInResult(GoogleSignInResult signInResult)
    {
        result = signInResult;
    }
}
