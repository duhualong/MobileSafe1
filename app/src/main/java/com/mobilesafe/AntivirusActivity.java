package com.mobilesafe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class AntivirusActivity extends AppCompatActivity {

    private ImageView iv_scanning;
    private TextView tv_init_virus;
    private ProgressBar pb;
    private LinearLayout ll_content;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.activity_antivirusa);
        iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
        tv_init_virus = (TextView) findViewById(R.id.tv_init_virus);

        pb = (ProgressBar) findViewById(R.id.progressBar1);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        /**
         * 第一个参数表示开始的角度
         * 第二个参数表示结束的角度
         * 第三个参数表示参照自己
         * 初始化旋转动画
         */
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        // 设置动画的时间
        rotateAnimation.setDuration(5000);
        // 设置动画无限循环 Animation.INFINITE=-1
        rotateAnimation.setRepeatCount(-1);
        // 开始动画
        iv_scanning.startAnimation(rotateAnimation);
    }

}
