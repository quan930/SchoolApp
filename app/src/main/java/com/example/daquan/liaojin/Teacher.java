package com.example.daquan.liaojin;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private String className;//课程1
    private String credit;//学分2
    private String teachingMethod;//授课方式3
    private String courseType;//课程类别4
    private String classNumber;//上课班号5
    private String classClass;//上课班级6
    private String classPerson;//上课人数7
    private List<String> times = new ArrayList<>();//时间8
    private List<String> sites = new ArrayList<>();//地点9

    public String getClassName() {
        return className;
    }

    public void add(String message) {
        String as[]=message.split(":");
        String messageNum = as[0];//消息指令
        String newMessage = as[1];
        if(messageNum.equals("1")) {
            className=newMessage;
        }else {
            if(messageNum.equals("2")) {
                credit=newMessage;
            }else {
                if(messageNum.equals("3")) {
                    teachingMethod=newMessage;
                }else {
                    if(messageNum.equals("4")) {
                        courseType=newMessage;
                    }else {
                        if(messageNum.equals("5")) {
                            classNumber=newMessage;
                        }else {
                            if(messageNum.equals("6")) {
                                classClass=newMessage;
                            }else {
                                if(messageNum.equals("7")) {
                                    classPerson=newMessage;
                                }else {
                                    if(messageNum.equals("8")) {
                                        times.add(newMessage);
                                    }else {
                                        if(messageNum.equals("9")) {
                                            sites.add(newMessage);
                                        }else {
                                            System.out.println("hello world");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
