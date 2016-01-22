package com.mobilesafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ` on 2016/1/21.
 * 短信备份的工具类
 */
public class SmsUtils {
    public static boolean bakUp(Context context, ProgressDialog pd) {

        //备份短信 1判断拥护手机上面是否有sd卡   2权限--   使用内容观察者   3写短信（写到sd卡）

        //判断sd卡的状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果能进来，说明sd卡存在
            ContentResolver resolver = context.getContentResolver();
            //获取短信的路径
            Uri uri = Uri.parse("content://sms/");
            //type=1 接收短信
            //type=2 发送短信
            //cursor 表示游标的意思
            Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
            //获取一共有多少短信

            int count=cursor.getCount();
            pd.setMax(count);
            int process=0;
            try {
                //把短信备份到sd卡，第二个参数表示名字
                File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
                //写文件
                FileOutputStream os = new FileOutputStream(file);
                //得到序列化器  pull解析
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(os, "utf-8");
                //standalone表示当前的xml是否独立true表示独立
                serializer.startDocument("utf-8", true);
                //设置开始的节点
                serializer.startTag(null, "smss");
                //设置smss节点上的属性值
                serializer.attribute(null,"size",String.valueOf(count));
                while (cursor.moveToNext()) {
                    System.out.println("-----------------------------------------");
                    System.out.println("address==" + cursor.getString(0));
                    System.out.println("date==" + cursor.getString(1));
                    System.out.println("type==" + cursor.getString(2));
                    System.out.println("body==" + cursor.getString(3));
                    System.out.println("-----------------------------------------");
                    serializer.startTag(null, "sms");
                    serializer.startTag(null, "address");
                    serializer.text(cursor.getString(0));
                    serializer.endTag(null, "address");
                    serializer.startTag(null, "data");
                    serializer.text(cursor.getString(1));
                    serializer.endTag(null, "data");
                    serializer.startTag(null, "type");
                    serializer.text(cursor.getString(2));
                    serializer.endTag(null, "type");
                    serializer.startTag(null, "body");
                    serializer.text(cursor.getString(3));
                    serializer.endTag(null, "body");
                    serializer.endTag(null, "sms");
                    //序列化一条短信后++
                    process++;
                    pd.setProgress(process);
                    SystemClock.sleep(20);

                }
                cursor.close();
                serializer.endTag(null, "smss");
                serializer.endDocument();
                os.flush();
                os.close();
                return  true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
