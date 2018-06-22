package com.example.daquan.liaojin;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//浮窗类
public class ShowPup {
    //浮窗
    public void showPopup(final Context context, View view, final Teacher teacherMes){
        final Dialog dialog = new Dialog(context);
        //popupWindow.setOutsideTouchable(true);
        dialog.setCanceledOnTouchOutside(false);//外部触摸
        List<String> messages = new ArrayList<>();
        ArrayAdapter<String> popAdapter;
        ListView popListView;
        TextView close;//关闭
        TextView className;//课程1
        TextView credit;//学分2
        TextView teachingMethod;//授课方式3
        TextView courseType;//课程类别4;

        dialog.setContentView(R.layout.pop);
        close = (TextView) dialog.findViewById(R.id.close);
        className = dialog.findViewById(R.id.pop_className);
        credit = dialog.findViewById(R.id.pop_credit);
        teachingMethod = dialog.findViewById(R.id.pop_teachingMethod);
        courseType = dialog.findViewById(R.id.pop_courseType);
        popListView = dialog.findViewById(R.id.teach_listView);

        className.setText(teacherMes.getClassName());
        credit.setText(teacherMes.getCredit());
        teachingMethod.setText(teacherMes.getTeachingMethod());
        courseType.setText(teacherMes.getCourseType());

        for(int i = 0;i < teacherMes.getClassClasses().size();i++){
            messages.add(teacherMes.getClassClasses().get(i).getClassClass()+"\t\t"+teacherMes.getClassClasses().get(i).getClassPerson()+"人");
        }

        popAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,messages);
        popListView.setAdapter(popAdapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//listview点击时间
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showTimesSites(context,teacherMes.getClassClasses().get(position));
            }
        });
        dialog.show();
    }
    //时间地点浮窗
    public void showTimesSites(Context context,Teacher.ClassClass classClass){
        final Dialog dialogSon = new Dialog(context);
        dialogSon.setContentView(R.layout.pop_time);
        dialogSon.setCanceledOnTouchOutside(false);//外部触摸

        TextView close;//关闭
        ListView popListView;
        ArrayAdapter<String> popAdapter;
        List<String> messages = new ArrayList<>();

        close = dialogSon.findViewById(R.id.close_1);
        popListView = dialogSon.findViewById(R.id.ListView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSon.dismiss();
            }
        });
        for(int i = 0;i < classClass.getSites().size();i++){
            messages.add(classClass.getTimes().get(i)+"\t"+classClass.getSites().get(i));
        }
        popAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,messages);
        popListView.setAdapter(popAdapter);
        dialogSon.show();
    }
}
