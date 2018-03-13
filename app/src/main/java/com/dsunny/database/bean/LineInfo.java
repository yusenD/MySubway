package com.dsunny.database.bean;

/**
 * Created by yusenDong on 2018/3/8.
 */

public class LineInfo {

    public String lid;// 线路ID
    public String sname;// 线路名称
    public String stime;// 首发车时间
    public String etime;// 末班车时间

    @Override
    public String toString() {
        return "Station{" +
                "lis='" + lid + '\'' +
                ", sname='" + sname + '\'' +
                ", stime='" + stime + '\'' +
                ", etime='" + etime + '\'' +
                '}';
    }

}
