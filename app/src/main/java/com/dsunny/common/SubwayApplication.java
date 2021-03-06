package com.dsunny.common;

import android.app.Application;

import com.dsunny.util.AppUtil;
import com.dsunny.util.SharedPreferencesUtil;
import com.infrastructure.cache.CacheManager;

import java.io.File;

/**
 * 本地Application
 */
public class SubwayApplication extends Application {


    private static SubwayApplication instance;

    public static SubwayApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 拷贝数据库
        initDataBase();
    }

    /**
     * 版本升级初始化数据库
     */
    private void initDataBase() {
        final int appVersionCode = AppUtil.getVersionCode(this);
        final int localVersionCode = SharedPreferencesUtil.getAppVersionCode();
        if (appVersionCode != localVersionCode) {
            // 版本更新，删除旧数据库
            File file = new File(AppConstants.SUBWAY_DB_FILE_PATH);
            if (file.exists()) {
                file.delete();
            }
            // 拷贝新的数据库
            AppUtil.copyDBFile(this);
            // 将VersionCode写入本地SharedPreferences
            SharedPreferencesUtil.saveAppVersionCode(appVersionCode);
        }
    }

}
