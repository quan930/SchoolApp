package com.example.daquan.liaojin;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Teacher {//解析消息
    private String className;//课程1
    private String credit;//学分2
    private String teachingMethod;//授课方式3
    private String courseType;//课程类别4
    private String classNumber;//上课班号5//无用
    private List<ClassClass> classClasses = new ArrayList<>();//上课班级集合
    private ClassClass aClass = new ClassClass();

    public String getCourseType() {

        return courseType;
    }

    public String getClassNumber() {

        return classNumber;
    }

    public String getTeachingMethod() {

        return teachingMethod;
    }

    public String getCredit() {

        return credit;
    }

    public String getClassName() {
        return className;
    }

    public List<ClassClass> getClassClasses() {
        return classClasses;
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
                            if(messageNum.equals("6")) {//上课班级
                                aClass = new ClassClass();
                                classClasses.add(aClass);
                                aClass.setClassClass(newMessage);
                            }else {
                                if(messageNum.equals("7")) {//上课人数
                                    aClass.setClassPerson(newMessage);
//                                    classPerson=newMessage;
                                }else {
                                    if(messageNum.equals("8")) {
//                                        times.add(newMessage);
                                        aClass.addTimes(newMessage);
                                    }else {
                                        if(messageNum.equals("9")) {
//                                            sites.add(newMessage);
                                            aClass.addSites(newMessage);
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
    class ClassClass{
        private String classClass;//上课班级6
        private String classPerson;//上课人数7
        private List<String> times = new ArrayList<>();//时间8
        private List<String> sites = new ArrayList<>();//地点9

        public void setClassPerson(String classPerson) {
            this.classPerson = classPerson;
        }

        public String getClassPerson() {
            return classPerson;
        }

        public String getClassClass() {

            return classClass;
        }

        public void setClassClass(String classClass) {

            this.classClass = classClass;

        }

        public List<String> getTimes() {
            return times;
        }

        public List<String> getSites() {

            return sites;
        }

        public void addTimes (String time){

            times.add(time);
        }
        public void addSites(String site){
            sites.add(site);
        }

    }
}
