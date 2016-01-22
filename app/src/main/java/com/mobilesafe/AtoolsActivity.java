package com.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.mobilesafe.utils.SmsUtils;
import com.mobilesafe.utils.UIUtils;

/**
 *
 * 高级工具
 */
public class AtoolsActivity extends Activity {

    private ProgressDialog pd;
    private ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        progressBar1 = (ProgressBar) findViewById(R.id.progressbar1);
    }

    /**
     * 点击事件，进入号码归属地查询页面
     * @param view
     */
    public  void numberQuery(View view){
        Intent intent=new Intent(this,NumberAddressQueryActivity.class);
        startActivity(intent);

    }

    /**
     * 短信备份
     * @param view
     */
    public  void backUpsms(View view){
//        //初始化一个进度条的对话框
       pd = new ProgressDialog(AtoolsActivity.this);
       pd.setTitle("提示");
       pd.setMessage("正在备份中，请稍后。。。。");
      pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = SmsUtils.bakUp(AtoolsActivity.this, new SmsUtils.BackUpCallBackSms() {

                    @Override
                    public void onBackUpSms(int process) {
                        pd.setProgress(process);

                    }

                    @Override
                    public void befor(int count) {
                        pd.setMax(count);

                    }
                });
                if (result) {
//                    Looper.prepare();
//                    Toast.makeText(AtoolsActivity.this,"备份成功",Toast.LENGTH_SHORT).show();
//                    Looper.loop();
                    UIUtils.showToast(AtoolsActivity.this,"备份成功");
                }else {
//                    Looper.prepare();
//                    Toast.makeText(AtoolsActivity.this,"备份失败",Toast.LENGTH_SHORT).show();
//                    Looper.loop();
                    UIUtils.showToast(AtoolsActivity.this,"备份失败");

                }
                pd.dismiss();
            }
        }).start();

    }
}
