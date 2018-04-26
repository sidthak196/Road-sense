package com.servicelearning.sidthakur.roadsensor;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by siddharth thakur on 10/29/2017.
 */

public class WriteToFile {

    static final private String DIRECTORY_NAME = Environment.getExternalStorageDirectory().getAbsolutePath();
    static final private String FILE_NAME = "GyroData";
    private static int count = 0;

    public static int appendToFile(String line) {
        count++;

        Log.d("WriteToFile", DIRECTORY_NAME + "/" + FILE_NAME);

        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(new File(DIRECTORY_NAME), FILE_NAME + ".txt");
        try {

            if (out.exists() == false) {
                out.createNewFile();
            }

            outStream = new FileOutputStream(out, true);
            outStreamWriter = new OutputStreamWriter(outStream);

            outStreamWriter.append(line + ",\n");
            outStreamWriter.flush();
        } catch (IOException e) {

            Log.e("WRITETOFILE", "Error in writing to file :" + e.getLocalizedMessage());
            return 0;
        }
        if (count == 100) {
            count = 0;
            return 2;
        }
        return 1;
    }

    public static ArrayList readData() {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        File in = new File(new File(DIRECTORY_NAME), FILE_NAME + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(in));
            String temp;
            while ((temp = br.readLine()) != null) {
                arrayList.add(new JSONObject(temp));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Write to file : ", "Json invalid");
            e.printStackTrace();
        }

        return arrayList;
    }
}
