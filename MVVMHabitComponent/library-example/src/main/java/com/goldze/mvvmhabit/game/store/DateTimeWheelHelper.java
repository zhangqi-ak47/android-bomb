package com.goldze.mvvmhabit.game.store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeWheelHelper {

    public int startYear=2020, endYear=1990;
    public int month=12, day=31, hour=23, minute=59, seconds=59;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM");

    public int stYear, stMonth, stDay, stHour, stMinute, stSeconds;
    public String yValue, mValue, dValue, hValue, nValue, sValue;

    public List<String> ltYear=new ArrayList<>(), ltMonth=new ArrayList<>(), ltDay=new ArrayList<>(), ltHour=new ArrayList<>(), ltMinute=new ArrayList<>(), ltSeconds=new ArrayList<>();

    public void initStartYear(){
        String s1=simpleDateFormat.format(new Date());
        String s2[]=s1.split("-");
        startYear=Integer.parseInt(s2[0]);
    }

    public void initToday(){
        String s1=simpleDateFormat.format(new Date());
        String s2[]=s1.split("-");
        yValue=Integer.parseInt(s2[0])+"年";
        mValue=Integer.parseInt(s2[1])+"月";
        dValue=Integer.parseInt(s2[2])+"日";
//        int h=Integer.parseInt(s2[3]);
//        int n=Integer.parseInt(s2[4]);
//        int s=Integer.parseInt(s2[5]);

        day=getDaysOfMonth(yValue, mValue);
        initDay();

        stYear=findValue(ltYear, yValue);
        stMonth=findValue(ltMonth, mValue);
        stDay=findValue(ltDay, dValue);
//        stHour=findValue(ltHour, h);
//        stMinute=findValue(ltMinute, n);
//        stSeconds=findValue(ltSeconds, s);
    }

    public int findValue(List list, String value){
        for (int i=0; i<list.size(); i++){
            if (value.equals(list.get(i))){
                return i;
            }
        }
        return 0;
    }

    public int getDaysOfMonth(String year, String month) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat2.parse(year.replace("年", "")+"-"+month.replace("月", "")));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 31;
    }

    public void initList(){
        ltYear.clear();
        for (int i=startYear; i>=endYear; i--){
            ltYear.add(i+"年");
        }
        ltMonth.clear();
        for (int i=1; i<=month; i++){
            ltMonth.add(i+"月");
        }
        ltDay.clear();
        for (int i=1; i<=day; i++){
            ltDay.add(i+"日");
        }
//        ltHour.clear();
//        for (int i=0; i<=hour; i++){
//            ltHour.add(i);
//        }
//        ltMinute.clear();
//        for (int i=0; i<=minute; i++){
//            ltMinute.add(i);
//        }
//        ltSeconds.clear();
//        for (int i=0; i<=seconds; i++){
//            ltSeconds.add(i);
//        }
    }

    public void initDay(){
        ltDay.clear();
        for (int i=1; i<=day; i++){
            ltDay.add(i+"日");
        }
    }

    public void dealDay(){
        day=getDaysOfMonth(yValue, mValue);
        initDay();
        stDay=findValue(ltDay, dValue);
        dValue=ltDay.get(stDay);
    }

}
