package com.mobilesafe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilesafe.bean.TaskInfo;
import com.mobilesafe.engine.TaskInfoParser;
import com.mobilesafe.utils.SystemInfoUtils;

import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {

    private TextView tv_task_process_count;
    private TextView tv_task_memory;
    private ListView list_view;
    private long totalMem;
    private long availMem;
    private List<TaskInfo> taskInfos;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                taskInfos = TaskInfoParser.getTaskInfos(TaskManagerActivity.this);
                handler.sendEmptyMessage(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TaskManagerAdapter adapter=new TaskManagerAdapter();
                        list_view.setAdapter(adapter);
                    }
                });

            }
        }).start();

    }

    /**
     * 区别
     * <p/>
     * ActivityManager
     * 活动管理器（任务管理器）
     * <p/>
     * packageManager
     * 包管理器
     */
    private void initUI() {
        setContentView(R.layout.activity_task_manager);
        tv_task_memory = (TextView) findViewById(R.id.tv_task_memory);
        tv_task_process_count = (TextView) findViewById(R.id.tv_task_process_count);
        list_view = (ListView) findViewById(R.id.list_view);

        int processCount = SystemInfoUtils.getProcessCount(this);
        tv_task_process_count.setText("运行中的进程：" + processCount + "个");

        availMem = SystemInfoUtils.getAvailMem(this);

        totalMem = SystemInfoUtils.getTotalMem(this);

        tv_task_memory.setText("剩余/总内存:"
                + Formatter.formatFileSize(TaskManagerActivity.this, availMem)
                + "/" + Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

    }

 static  class ViewHolder{
     ImageView iv_app_icon;
     TextView tv_app_name;
     TextView tv_app_memory_size;
     CheckBox tv_app_status;
    }

    private class TaskManagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
            ViewHolder holder=new ViewHolder();
            holder.iv_app_icon= (ImageView) view.findViewById(R.id.iv_app_icon);
            holder.tv_app_name= (TextView) view.findViewById(R.id.tv_app_name);
            holder.tv_app_memory_size= (TextView) view.findViewById(R.id.tv_app_memory_size);
            holder.tv_app_status= (CheckBox) view.findViewById(R.id.tv_app_status);
          TaskInfo taskInfo=taskInfos.get(position);
            holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());
            holder.tv_app_name.setText(taskInfo.getPackageName());
            holder.tv_app_memory_size.setText(Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemorySize()));


            return view;
        }
    }
}