package com.dsunny.activity.base;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.dsunny.common.AppConstants;
import com.dsunny.subway.R;
import com.infrastructure.activity.BaseActivity;
import com.infrastructure.request.RequestCallback;

/**
 * 封装
 */
public abstract class AppBaseActivity extends BaseActivity {

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 抽象回调类
     */
    protected abstract class AppRequestCallback implements RequestCallback {
        @Override
        public void onSuccess(String content) {

        }

        @Override
        public void onFail(String errorMsg) {
        }

        @Override
        public void onCookieExpired() {
        }

    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     */
    protected void startAppActivity(String activity) {
        startAppActivity(activity, null);
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param intent   请求Intent
     */
    protected void startAppActivity(String activity, Intent intent) {
        try {
            Class activityClass = Class.forName(activity);
            if (intent == null) {
                startActivity(new Intent(mContext, activityClass));
            } else {
                intent.setClass(mContext, activityClass);
                startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_line:
                startAppActivity(AppConstants.ACTIVITY_LINE_SEARCH);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
