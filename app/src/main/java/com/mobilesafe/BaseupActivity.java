package com.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ` on 2016/1/12.
 */
public abstract class BaseupActivity extends Activity {
    //定义一个手势识别器
    private GestureDetector detector;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        //实例化这个手势识别器
        detector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            /**
             * 当我们手指在上面滑动的时候回调
             * @param e1
             * @param e2
             * @param velocityX
             * @param velocityY
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //屏蔽在x滑动很慢的情形
              if (Math.abs(velocityX)<150){
                  Toast.makeText(getApplicationContext(),"滑动的太慢",Toast.LENGTH_SHORT).show();
                  return true;
              }
                //屏蔽斜着划
           if (Math.abs((e2.getRawY() - e1.getRawY()))>150){
               Toast.makeText(getApplication(),"不能这样划",Toast.LENGTH_SHORT).show();
                    return true;
                }

                if ((e2.getRawX()-e1.getRawX())>200){
                    //显示上一个页面，从左往右滑动
                    System.out.println("显示上一个页面，从左往右滑动");
                    showPre();
                    return true;
                }
                if ((e1.getRawX()-e2.getRawX())>200){
                    //显示下一个页面，从右向左滑动
                    System.out.println("显示下一个页面，从右向左滑动");
                    showNext();
                    return true;

                }
                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });
    }
    public abstract void showNext();
    public abstract void showPre();
    /**
     * 下一步的点击事件
     * @param view
     */
    public void next(View view){
        showNext();

    }
    /**
     *   上一步
     * @param view
     */
    public void pre(View view){
        showPre();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
