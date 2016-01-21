package com.mobilesafe.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesafe.R;
import com.mobilesafe.adapter.MyBaseAdapter;
import com.mobilesafe.bean.BlackNumberInfo;
import com.mobilesafe.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity2 extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> blackNumberInfos;
    private LinearLayout ll_pd;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;
    //当前页面
    private  int mCurrentPagenumber=0;
    //每页展示20条数据
    private  int mPageSize=20;
    private TextView tv_page_number;
    //一共多少页面
    private int totalPage;
    private EditText et_page_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        initUI();
        initData();
    }
private Handler handler=new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        ll_pd.setVisibility(View.INVISIBLE);
        tv_page_number.setText(mCurrentPagenumber + "/" + totalPage);
        adapter = new CallSafeAdapter(blackNumberInfos,CallSafeActivity2.this);
        list_view.setAdapter(adapter);
        return false;
    }
});
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //通过总记录数/每页多少条数据
                dao = new BlackNumberDao(CallSafeActivity2.this);
                totalPage = dao.getTotalNumber()/mPageSize;
              //  tv_page_number.setText(mCurrentPagenumber/totalPage+"");
           //查询所有数据     blackNumberInfos = dao.findAll();
              blackNumberInfos=dao.findPar(mCurrentPagenumber, mPageSize);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void initUI() {
        ll_pd = (LinearLayout) findViewById(R.id.ll_pb);
        ll_pd.setVisibility(View.VISIBLE);
        list_view = (ListView) findViewById(R.id.list_view);
        tv_page_number = (TextView) findViewById(R.id.tv_page_number);
        et_page_number = (EditText)findViewById(R.id.et_page_number);
    }
    private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo>{

        private CallSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView= View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);
                holder=new ViewHolder();
                 holder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
               holder.tv_mode= (TextView) convertView.findViewById(R.id.tv_mode);
               holder.iv_detele= (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            }else {
               holder= (ViewHolder) convertView.getTag();
            }

           holder.tv_number.setText(lists.get(position).getNumber());
            String mode=lists.get(position).getMode();
            if (mode.equals("1")){
                holder.tv_mode.setText("来电+短信拦截");
            }else if (mode.equals("2")){
                holder.tv_mode.setText("电话拦截");
            }else if (mode.equals("3")){
                holder.tv_mode.setText("短信拦截");
            }
           final BlackNumberInfo info=lists.get(position);
            holder.iv_detele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  String number=info.getNumber();
                boolean  result=dao.delete(number);
                    if (result){
                        Toast.makeText(CallSafeActivity2.this,"删除成功",Toast.LENGTH_SHORT).show();
                        lists.remove(info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(CallSafeActivity2.this,"删除失败",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            return convertView;
        }

    }
    static  class ViewHolder{
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_detele;

    }

    /**
     * 上一页
     * @param view
     */
    public void prePage(View view){
        if (mCurrentPagenumber<=0){
           Toast.makeText(this,"已经是第一页了",Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentPagenumber--;
        initData();

    }
    /**
     * 下一页
     * @param view
     */
    public  void nextPage(View view){
        //判断当前页码不能大于总页面
    if (mCurrentPagenumber>=(totalPage-1)){
        Toast.makeText(this,"这已经是最后一页",Toast.LENGTH_LONG).show();
        return;
    }
        mCurrentPagenumber++;
        initData();
    }
    /**
     * 跳转页
     * @param view
     */
    public  void jump(View view){
        String str_page_number=et_page_number.getText().toString().trim();
        if (TextUtils.isEmpty(str_page_number)){
            Toast.makeText(this,"请输入正确页码",Toast.LENGTH_SHORT).show();
        }else {
          int number= Integer.parseInt(str_page_number);
            if (number>=0&&number<=(totalPage-1)){
                mCurrentPagenumber=number;
                initData();

            }else {
                Toast.makeText(this,"请输入有效的页码",Toast.LENGTH_SHORT).show();
            }
        }



    }
}
