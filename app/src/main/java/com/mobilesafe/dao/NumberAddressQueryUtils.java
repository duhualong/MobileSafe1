package com.mobilesafe.dao;

/**
 * Created by ` on 2016/1/14.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class NumberAddressQueryUtils  {
    private  static String  path="data/data/com.mobilesafe/files/address.db";
    /**
     * 传一个号码进来，返回一个归属地
     */
    public  static  String queryNumber(String number){
        String address=number;
        //path 把address.db这个数据库拷贝到data/data/《包名》/file/address.db
        //手机号码13,15,18，正则表达式
        SQLiteDatabase database= SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        if(number.matches("^[1]([3-8][0-9]{1}|59|58|88|89)[0-9]{8}$")){
           //手机号码
           Cursor cursor= database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});

           while (cursor.moveToNext()){
               String location= cursor.getString(0);
               address=location;

           }
           cursor.close();
        }else {
           //其他的电话号码
           switch (number.length()){
               case 3:
                   address="匪警，火警，公共号码！";
                   break;
               case 4:
                   address="模拟器";
                   break;
               case 5:
                   address="移动、联通、电信客服号码";
                   break;
               case 7:
                   address="本地号码";
                   break;
               case 8:
                   address="本地号码";
                   break;
               default:
                   //处理长途电话
                   if (number.length()>10&&number.startsWith("0")){
                      Cursor cursor= database.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 3)});
                       while (cursor.moveToNext()){
                           String location=cursor.getString(0);
                           address=location.substring(0,location.length()-2);
                       }
                       cursor.close();
                      cursor= database.rawQuery("select location from data2 where area=?", new String[]{number.substring(1, 4)});
                       while (cursor.moveToNext()){
                           String location=cursor.getString(0);
                           address=location.substring(0,location.length()-3);
                       }

                   }
                   break;
           }
       }

        return  address;
    }
}
