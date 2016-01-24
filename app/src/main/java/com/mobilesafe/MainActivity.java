package com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesafe.utils.Tools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String TAG ="MainActivity" ;
    private static final int SHOE_UPDATE_DIALOG =0;
    private static final int ENTER_HOME =1;
    private static final int URL_ERROR =2;
    private static final int NETWORK_ERROR =3;
    private static final int JSON_ERROR =4;
    private TextView tv_main_version;
    private String description;
    private String aplurl;
    private  TextView tv_update_info;
    private SharedPreferences sp;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOE_UPDATE_DIALOG:
                    Log.i(TAG, "显示升级的对话框");
                    showupdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "联网成功", Toast.LENGTH_SHORT).show();
                    break;
                case URL_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "url出错了", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "联网异常", Toast.LENGTH_SHORT).show();
                    break;
                case JSON_ERROR:
                    enterHome();
                    Toast.makeText(MainActivity.this, "JSON出错了", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        tv_main_version= (TextView) findViewById(R.id.tv_main_version);
        tv_main_version.setText("版本号" + getVersionName());
        tv_update_info= (TextView) findViewById(R.id.tv_update_info);
        boolean update=sp.getBoolean("update",false);
        //拷贝数据库
        copyDB();
        //创建快捷方式
        createShortcut();
        if (update){
            //检查升级
            checkupdate();
        }else {
            //自动升级已经关闭
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            },2000);
     }


        AlphaAnimation aa=new AlphaAnimation(0.2f,1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_main).startAnimation(aa);

    }

    /**
     * 快捷fangs
     */
    private void createShortcut() {
        Intent intent=new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //如果设置为true表示可以创建重复的快捷方式
        intent.putExtra("duplicate",false);

        /**
         * 干什么事情
         * 叫什么名字
         * 长成什么样子
         */
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(), R.drawable.head));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "清道夫");

//干什么事情
        /**
         * 不能使用显式意图，必须使用隐式意图
         */
        Intent shortcut_intent=new Intent();
        shortcut_intent.setAction("aaa.bbb.ccc");
        shortcut_intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortcut_intent);
        sendBroadcast(intent);
    }

    /**
     * path 把address.db这个数据库拷贝到data/data/《包名》/file/address.db
     */

    private void copyDB() {
        //只要拷贝一次，我就不要你再拷贝
        try {
            File file=new File(getFilesDir(),"address.db");
            if (file.exists()&&file.length()>0){
                //正常不需要拷贝
                Log.i(TAG,"不需要拷贝");
            }else{
                InputStream is= getAssets().open("address.db");
                FileOutputStream fos=new FileOutputStream(file);
                byte[] buffer=new byte[1024];
                int len=0;
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                is.close();
                fos.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出升级对话框
     */
    private void showupdateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示升级");
      //  builder.setCancelable(false);//强制升级
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //进入主页面
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(description);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载APK，并替换安装
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    //sdcard存在
                    FinalHttp finalhttp=new FinalHttp();
                    finalhttp.download(aplurl, Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {
                                @Override
                                public void onSuccess(File t) {
                                    super.onSuccess(t);
                                    installAPK(t);
                                }

                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                @Override
                                public void onLoading(long count, long current) {

                                    super.onLoading(count, current);
                                    tv_update_info.setVisibility(View.VISIBLE);
                                    //当前加载百分比
                                    int progress= (int) (current*100/count);
                                    tv_update_info.setText("下载进度"+progress+"%");

                                }
                            });
                }else {
                    Toast.makeText(getApplicationContext(),"没有sdcard，请安装再试",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();

    }

    /**
     * 安装apk
     */
    private void installAPK(File t) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void enterHome() {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void checkupdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message mes=Message.obtain();
                long startTime=System.currentTimeMillis();
                //URL http://192.168.20.36:8080/updateinfo.html
                try {
                    URL url=new URL(Constants.SERVER_URL);
                    //联网
                    HttpURLConnection con= (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(4000);
                    con.setDoInput(true);
                    int code=con.getResponseCode();
                    if (code==200){
                        //联网成功
                        InputStream is=con.getInputStream();
                        //把流转成String类型
                        String result= Tools.readFromStream(is);
                        Log.i(TAG,"联网成功"+result);
                        //json解析
                        JSONObject obj=new JSONObject(result);
                        //得到服务器的版本信息
                        String version= (String) obj.get("version");
                        description=(String) obj.get("description");
                        aplurl=(String) obj.get("aplurl");
                        //检验是否有新版本
                        if(getVersionName().equals(version)){
                            //版本一致，没有新版本
                            mes.what=ENTER_HOME;

                        }else{
                            //有新版本，弹出升级对话框
                            mes.what=SHOE_UPDATE_DIALOG;

                        }

                    }else{
                        Log.i(TAG, "联网失败");
                    }
                } catch (MalformedURLException e) {
                    mes.what=URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    mes.what=NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    mes.what=JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    long endTime=System.currentTimeMillis();
                    long dTime=endTime-startTime;
                    if (dTime<2000){
                        try {
                            Thread.sleep(2000-dTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(mes);
                }
            }

        }).start();
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName(){
        //用来管理手机的APK
        PackageManager pm=getPackageManager();
        try {
            //得到知道APK的功能清单文件
           PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
           return info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
