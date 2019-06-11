package com.youdian.shixun02;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.youdian.shixun02.Datamanipulation.Constant;
import com.youdian.shixun02.R;

import com.youdian.shixun02.Datamanipulation.ConnectTask;
import com.youdian.shixun02.Datamanipulation.Data;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Switch toggleButton;
    private TextView  wendu;
    private TextView  shidu;
    private TextView  yanwu;
    private TextView  guanzhao;
    private Button button;
    private Data data;
    private Context context;
    private ConnectTask connectTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化UI
        initView();
        //绑定事件
        initevent();
        //初始化数据
        Dataone();

    }
    private void initView(){

        toggleButton=(Switch)findViewById(R.id.toggle_bt);
        wendu=(TextView)findViewById(R.id.wendu);
        shidu=(TextView)findViewById(R.id.shidu);
        yanwu=(TextView)findViewById(R.id.yanwu);
        guanzhao=(TextView)findViewById(R.id.guanzhao);
        button=(Button)findViewById(R.id.button);

    }
    private void initevent(){
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("==========", "ip=" + Constant.IP + "   port=" + Constant.port);
                    // 开启任务
                    connectTask = new ConnectTask(context, data, wendu, shidu,yanwu,guanzhao);
                    connectTask.setCIRCLE(true);
                    connectTask.execute();
                    Toast.makeText(getApplication(), "按钮打开", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // 取消任务
                    if (connectTask != null && connectTask.getStatus() == AsyncTask.Status.RUNNING) {
                        connectTask.setCIRCLE(false);
                        connectTask.setSTATU(false);
                        // 如果Task还在运行，则先取消它
                        connectTask.cancel(true);
                        try {
                            connectTask.getmSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplication(), "按钮关闭", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);

            }
        });

    }
    private void Dataone(){
        context=this;
        data=new Data();
        Toast.makeText(getApplication(), "正在连接。。。", Toast.LENGTH_SHORT)
                .show();

    }
}
