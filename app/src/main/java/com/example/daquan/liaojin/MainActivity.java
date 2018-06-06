package com.example.daquan.liaojin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    static Handler handler;
    private ImageView imageView;
    private ImageView imageTouch;
    private EditText editText;
    private Button button;
//    private Bitmap bm = null;
    private String url = "http://59.79.112.9/sys/ValidateCode.aspx";

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        imageTouch = findViewById(R.id.imageTouch);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        authCode(this,imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authCode(MainActivity.this,imageView);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchClass(MainActivity.this,imageTouch);
            }
        });


//        handler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                imageView.setImageBitmap((Bitmap) msg.obj);
//            }
//        };
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;
//                try {
//                    url = new URL(urlpath);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    // 4、初始化连接对象
//                    // 设置请求的方法，注意大写
//                    connection.setRequestMethod("GET");
//                    InputStream is = connection.getInputStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(is);//写入一个bitmap流
//                    Message messageal=handler.obtainMessage();
//                    messageal.what = 1;
//                    messageal.obj = bitmap;
//                    handler.sendMessage(messageal);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
    public void authCode(Context context, final ImageView image){
        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        ImageRequest imageRequest = new ImageRequest(url,listener,0,0,null,null,errorListener);
        Volley.newRequestQueue(context).add(imageRequest);
    }
    public void touchClass(Context context, final ImageView image){
        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        String urll = "http://59.79.112.9/ZNPK/TeacherKBFB_rpt.aspx"+"?Sel_XNXQ=20171&Sel_JS=0000222&type=1&txt_yzm="+editText.getText();
        ImageRequest imageRequest = new ImageRequest(urll,listener,0,0,null,null,errorListener);
        Volley.newRequestQueue(context).add(imageRequest);
    }
}
