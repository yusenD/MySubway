package com.dsunny.common;

import android.os.Environment;

import com.infrastructure.commom.BaseConstants;

/**
 * 本地常量
 */
public class AppConstants extends BaseConstants {
    //数据库名称和路径
    public static final String SUBWAY_DB_NAME = "subway.db";
    public static final String SUBWAY_DB_FILE_PATH = Environment.getDataDirectory() + "/data/com.dsunny.subway/subway.db";
    // Activity路径
    public static final String ACTIVITY_SEARCH = "com.dsunny.activity.SearchActivity";
    public static final String ACTIVITY_DETAIL = "com.dsunny.activity.DetailActivity";
    public static final String ACTIVITY_LINE_SEARCH = "com.dsunny.activity.LineActivity";
    public static final String ACTIVITY_LINE_Detail = "com.dsunny.activity.LineDetailActivity";
    // Activity换乘传值
    public static final String KEY_TRANSFER_DETAIL = "transferdetail";
    //线路ID传值
    public static final String KEY_LINE_DETAIL = "lid";
}
