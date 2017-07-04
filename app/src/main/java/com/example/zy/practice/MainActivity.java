package com.example.zy.practice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private List<Bean.DataBean.EssayBean> list;
    private MyAdapter adapter;

    
    private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String url=msg.obj.toString();
                Gson gson=new Gson();
                Bean bean=gson.fromJson(url,Bean.class);
                list.addAll(bean.getData().getEssay());
                adapter.notifyDataSetChanged();
            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);

        list = new ArrayList<>();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        new Thread(){
            @Override
            public void run() {
                String url=Utils.getUrlConnect("http://v3.wufazhuce.com:8000/api/reading/index/?version=3.5.0&platform=android");
                Message msg=Message.obtain();
                msg.obj=url;
                handler.sendMessage(msg);
            }
        }.start();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView=View.inflate(MainActivity.this,R.layout.lv_item,null);

                holder=new ViewHolder();

                holder.text1=(TextView)findViewById(R.id.text1);
                holder.text2=(TextView)findViewById(R.id.text2);

                convertView.setTag(holder);
            }else{
              holder= (ViewHolder) convertView.getTag();

            }
            Bean.DataBean.EssayBean bean=list.get(position);
            holder.text1.setText(bean.getGuide_word());
            holder.text2.setText(bean.getHp_makettime());

            return convertView;
        }

        class ViewHolder{
            TextView text1;
            TextView text2;
        }
    }
}
