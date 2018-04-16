package com.servicelearning.sidthakur.roadsensor;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by siddharth thakur on 10/29/2017.
 */

public class WriteToFile {

    static final private String DIRECTORY_NAME = Environment.getExternalStorageDirectory().getAbsolutePath();
    static final private String FILE_NAME = "GyroData";

    public static int appendToFile(String line) {
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
        return 1;
    }

}
