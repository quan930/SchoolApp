package com.example.daquan.liaojin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText;
    private Button button;
    private ListView listView;
    private String session;//cookie
    private List<String> teacherNames;//教师名字
    private List<String> teacherIDs;//教师id
    private List<Teacher> teachers;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        autoCompleteTextView = findViewById(R.id.auto);
        listView = findViewById(R.id.listView);

        start();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImg();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeacherClass();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ShowPup().showPopup(MainActivity.this,view,teachers.get(position));
            }
        });
    }

    //获得session
    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://59.79.112.9/ZNPK/TeacherKBFB.aspx");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    String headerField = connection.getHeaderField("set-cookie");
                    Log.d("asd", headerField.toString());
                    session = headerField.split(";")[0];
                    Log.d("第一次连接", session);
                    getList();
                    getImg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //获取教师列表，id
    private void getList(){
        final ArrayAdapter<String> arr_adapter;
        teacherIDs = new ArrayList<>();
        teacherNames = new ArrayList<>();
        URL u2 = null;
        try {
            u2 = new URL("http://59.79.112.9/ZNPK/Private/List_JS.aspx?xnxq=20171&t=134");
            HttpURLConnection con2 = (HttpURLConnection) u2.openConnection();
            con2.setRequestProperty("Cookie", session);
            con2.setRequestProperty("Referer", "http://59.79.112.9/ZNPK/TeacherKBFB.aspx");

            con2.connect();

            //获取内容，内容中就包括id和name
            BufferedReader br2 = new BufferedReader(new InputStreamReader(con2.getInputStream(), "GBK"));
            String s2 = null;
            StringBuffer sb2 = new StringBuffer();
            while ((s2 = br2.readLine()) != null) {
                sb2.append(s2);
            }

            final String s = sb2.toString();
//		System.out.println(s);
            //数据格式如下：
            //<option value=0000368>Kerry Button</option>
            int p1 = 0;
            int p2 = 0;
            p1=s.indexOf("<option value=", 0);

            //遍历循环处理所有的id和name
            while(p1!=-1) {
                //假定id的长度为固定7位字符
                String id = s.substring(p1+14,p1+21);

                p2 = s.indexOf("</option>", p1);
                String name = s.substring(p1+22,p2);
                p1 = s.indexOf("<option value=", p2);

                teacherIDs.add(id);
                teacherNames.add(name);

            }
            Log.d("OK了", "getList: ");
            //适配器
            arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teacherNames);
            //设置样式
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //加载适配器
                    autoCompleteTextView.setAdapter(arr_adapter);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获得验证码
    private void getImg() {//验证码
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://59.79.112.9/sys/ValidateCode.aspx");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.addRequestProperty("Referer", "http://59.79.112.9/ZNPK/TeacherKBFB.aspx");//http://59.79.112.9/ZNPK/TeacherKBFB.aspx
                    connection.addRequestProperty("Cookie", session);

                    InputStream is = connection.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //教师课表
    private void getTeacherClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ArrayAdapter<String> adapter;
                    URL url = new URL("http://59.79.112.9/ZNPK/TeacherKBFB_rpt.aspx");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("Referer", "http://59.79.112.9/ZNPK/TeacherKBFB.aspx");
                    connection.addRequestProperty("Cookie", session);

                    Log.d("验证码",editText.getText().toString());
                    //判断教师名字输入框
                    if(autoCompleteTextView.getText().toString().equals("")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"名字不为空",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(!teacherNames.contains(autoCompleteTextView.getText().toString())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"名字错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        //post请求内容
                        String id = teacherIDs.get(teacherNames.indexOf(autoCompleteTextView.getText().toString()));//获得spinner列表索引
                        String postString = "Sel_XNXQ=20171&Sel_JS="+id+"&type=2&txt_yzm=" + editText.getText().toString();
                        String html = postMessage(connection,postString);

                        //判断验证码
                        if(html.contains("验证码错误！")){
                            Log.d("错误", "run: ");
                            getImg();//刷新验证码
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{//验证码正确逻辑
                            //处理响应数据
                            Document doc = Jsoup.parseBodyFragment(html);
                            List<String> names = processData(doc);
                            Log.d("课程信息",String.valueOf(teachers.size()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(teachers.size()==0){
                                        Log.d("没有课", "run: ");
                                        Toast.makeText(MainActivity.this,"没有课",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,names);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(adapter);
                                }
                            });
                        }
                    connection.disconnect();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //post响应数据,返回课程列表
    private List<String> processData(Document doc){
        Elements body = doc.getElementsByTag("table");
        teachers = new ArrayList<>();
        List<String> classNames;
        Teacher teacher = null;
        //数据解析
        int lineNum = 1;//行数
        for(int i = 0;i < body.size();i++) {
            Elements tds = body.get(i).select("td");
            Log.d("礼品",tds.text());
            if(tds.text().equals("环节")){
                break;
            }
            for(int j = 0;j < tds.size();j++){
                String oldClose = tds.get(j).text();
                Log.d("漂亮",oldClose);
                if(oldClose.equals("环节")){
                    break;
                }
                if(oldClose.equals(String.valueOf(lineNum))) {
                    if (tds.get(j+1).text().equals("7")){
                        break;
                    }
                    if(tds.get(j+1).text().equals("")) {//原课

                    }else {//新课
                        teacher = new Teacher();
                        Log.d("有一个课",tds.get(j+1).text());
                        teachers.add(teacher);
                    }
                    for(int k = 0;k < 9;k++) {
                        j++;
                        String asd = tds.get(j).text();
//                        Log.d("详情",String.valueOf(k)+":"+asd);
                        if(asd.equals("")) {

                        }else {
                            teacher.add((k+1)+":"+asd);
                        }
                    }
                    lineNum++;
                }else {
                    if(i<2) {
//                                    System.out.println(oldClose);
                    }
                }
            }
        }
        classNames = new ArrayList<>();
        for(int i = 0;i < teachers.size();i++){
            classNames.add(teachers.get(i).getClassName());
        }
        return classNames;
    }
    //post发数据接收数据，验证码正确返回true
    private String postMessage(HttpURLConnection connection,String postString){
        String message = "错误";
        try {
            //post
            OutputStream os = connection.getOutputStream();
            os.write(postString.getBytes());
            //接收
            InputStream is = connection.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, "GB2312"));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            //响应数据
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
//                Log.d("阿斯顿", line);
            }
            message=stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
