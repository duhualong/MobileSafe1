package com.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 修改归属地显示方法
 */
public class DragViewActivity extends Activity {
    private TextView tv_top;
    private TextView tv_bottom;
    private ImageView iv_drag;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private SharedPreferences sp;
    long[] mHits=new long[3];//数组长度表示要点击的次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_bottom= (TextView) findViewById(R.id.tv_bottom);
        tv_top= (TextView) findViewById(R.id.tv_top);
        iv_drag= (ImageView) findViewById(R.id.iv_drag);
        int lastX = sp.getInt("lastX", 0);
        int lastY = sp.getInt("lastY", 0);
        //获取屏幕的宽度和高度
       final int winWidth= getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight=getWindowManager().getDefaultDisplay().getHeight();
        if (lastY > winHeight / 2) {// 上边显示,下边隐藏
            tv_top.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.INVISIBLE);
        } else {
            tv_top.setVisibility(View.INVISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_drag
                .getLayoutParams();// 获取布局对象
        layoutParams.leftMargin = lastX;// 设置左边距
        layoutParams.topMargin = lastY;// 设置top边距

        iv_drag.setLayoutParams(layoutParams);// 重新设置位置
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();//开机后开始计算的时间
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    //把图标居中
                    iv_drag.layout(winWidth / 2 - iv_drag.getWidth() / 2, iv_drag.getTop(), winWidth / 2 + iv_drag.getWidth() / 2, iv_drag.getBottom());
                }
            }
        });

        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        // 计算移动偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新左上右下距离
                        int l = iv_drag.getLeft() + dx;
                        int r = iv_drag.getRight() + dx;
                        int t = iv_drag.getTop() + dy;
                        int b = iv_drag.getBottom() + dy;
                        if (l < 0 || r > winWidth || t < 0 || b > winHeight - 30) {
                            break;
                        }
                        if (t > winHeight / 2) {// 上边显示,下边隐藏
                            tv_top.setVisibility(View.VISIBLE);
                            tv_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                        }
                        // 更新界面
                        iv_drag.layout(l, t, r, b);
                        // 重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 记录坐标点
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("lastX", iv_drag.getLeft());
                        edit.putInt("lastY", iv_drag.getTop());
                        edit.apply();
                        break;
                    default:
                        break;
                }
                return false;//事件向下传递，让双击事件传递
            }
        });
    }

}
