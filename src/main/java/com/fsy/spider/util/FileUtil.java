package com.fsy.spider.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class FileUtil {

    public static void save(String str , String savePath){
        File file = new File(savePath);
        if(!file.exists()){
            try {

                //创建文件夹
                file.getParentFile().mkdirs();

                file.createNewFile();

                if(file.canWrite()){



                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    fileOutputStream.write(str.getBytes());

                    fileOutputStream.flush();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
