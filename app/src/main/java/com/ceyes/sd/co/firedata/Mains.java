package com.ceyes.sd.co.firedata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Mains extends AppCompatActivity {
    Button button,button1,button2;
    ImageView img_user,img_user2,img_user3,img_user4,img_user5;
    TextView tvText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        img_user2=(ImageView)findViewById(R.id.img_user2);
        tvText2=(TextView)findViewById(R.id.tvText2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "@drawable/ab";  // where myresource (without the extension) is the file

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                Drawable res = getResources().getDrawable(imageResource);
                img_user2.setImageDrawable(res);
                tvText2.setText("Sample text1");
                button.setBackgroundColor(Color.parseColor("#738b28"));
                button2.setBackgroundColor(Color.parseColor("#008577"));
                button1.setBackgroundColor(Color.parseColor("#008577"));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "@drawable/ab2";  // where myresource (without the extension) is the file

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                Drawable res = getResources().getDrawable(imageResource);
                img_user2.setImageDrawable(res);
                tvText2.setText("Sample text2");
                button.setBackgroundColor(Color.parseColor("#008577"));
                button2.setBackgroundColor(Color.parseColor("#008577"));
                button1.setBackgroundColor(Color.parseColor("#738b28"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "@drawable/ab";  // where myresource (without the extension) is the file

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                Drawable res = getResources().getDrawable(imageResource);
                img_user2.setImageDrawable(res);
                tvText2.setText("Sample text3");
                button.setBackgroundColor(Color.parseColor("#008577"));
                button2.setBackgroundColor(Color.parseColor("#738b28"));
                button1.setBackgroundColor(Color.parseColor("#008577"));
            }
        });
    }

}
