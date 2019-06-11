package com.youdian.shixun02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class AboutActivity extends AppCompatActivity {
   private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //绑定UI
        initView();
        //绑定事件
        initevent();
    }
    private  void  initView(){
        back=(ImageView)findViewById(R.id.back);
    }
    private void initevent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,MainActivity.class);
                startActivity(intent);
                AboutActivity.this.finish();
            }
        });
    }
}
