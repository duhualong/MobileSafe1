package com.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilesafe.R;

/**
 * Created by ` on 2016/1/9.
 */
public class SettingItemView extends RelativeLayout {
    private CheckBox cb_status;
    private TextView tv_desc;
    private TextView tv_update;
    private  String desc_on;
    private  String desc_off;
    /**
     * 初始化布局文件
     * @param context
     */
    private void iniView(Context context) {
        View.inflate(context, R.layout.setting_item_view,this);
        cb_status= (CheckBox) this.findViewById(R.id.cb_status);
        tv_desc= (TextView) this.findViewById(R.id.tv_desc);
        tv_update= (TextView) this.findViewById(R.id.tv_update);


    }
    public SettingItemView(Context context) {
        super(context);
        iniView(context);
    }

    /**
     * 检验组合控件是否右焦点
     */
    public boolean  isChecked(){
        return cb_status.isChecked();
    }

    /**
     *
     *设置组合控件的状态
     */
    public void setChecked(boolean checked){
        if(checked){
            setDesc(desc_on);
        }else {
            setDesc(desc_off);
        }


            cb_status.setChecked(checked);
    }

    /**
     * 设置组合控件的描述信息
     */

    public  void setDesc(String text){
        tv_desc.setText(text);
    }

    /**
     * 带有两个参数的构造方法，布局文件使用的时候调用
     * @param context
     * @param attrs
     */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);
        String title= attrs.getAttributeValue("http://schemas.android.com/apk/com.mobilesafe","title");
        desc_on= attrs.getAttributeValue("http://schemas.android.com/apk/com.mobilesafe","desc_on");
        desc_off= attrs.getAttributeValue("http://schemas.android.com/apk/com.mobilesafe","desc_off");
        tv_update.setText(title);
        setDesc(desc_off);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView(context);
    }
}
