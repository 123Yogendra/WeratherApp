package com.weather.utils;

import android.content.Context;
import android.os.Environment;

import com.weather.activity.BaseActivity;
import com.weather.activity.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class FileUtils {


    public static File getRootFile(Context context) {

        File f = new File(Environment.getExternalStorageDirectory(), "."+context
                .getString(R.string.app_name));
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return f;
    }



    /**
     * This method is used to write file in sdcard.
     */
    public static void writeResponseInToFile(Context context, String serverResponse) {

        try {
            File responseFile = new File(getRootFile(context), "response.txt");
            if (responseFile.exists())
                responseFile.delete();

            responseFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(responseFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(serverResponse);
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {
            ((BaseActivity) context).onException(e);
        }
    }

    /**
     * This method is used to read file from sdcard.
     */
    public static String readResponseFromFile(Context context) {

        try {
            File responseFile = new File(getRootFile(context), "response.txt");
            //Read text from file
            StringBuilder stringBuilder = new StringBuilder();


                BufferedReader br = new BufferedReader(new FileReader(responseFile));
                String line;

                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }
                br.close();

                return stringBuilder.toString();
        } catch (Exception e) {
            ((BaseActivity) context).onException(e);
        }
        return "";
    }

}
