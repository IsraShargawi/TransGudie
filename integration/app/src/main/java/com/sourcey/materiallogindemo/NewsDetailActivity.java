package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Khyati on 12/5/17.
 */
public class NewsDetailActivity extends AppCompatActivity {
    private TextView txtTitle,txtDate;
    private String title,date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            title=b.getString("title");
            date=b.getString("date");
        }
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        txtDate=(TextView)findViewById(R.id.txtDate);
        txtTitle.setText(title);
        txtDate.setText(date);
    }
}