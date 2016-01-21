package com.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ` on 2016/1/7.
 */
public class Tools {
    public static  String readFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        is.close();
        String result=baos.toString();
        baos.close();
        return  result;
    }
}
