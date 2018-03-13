package com.dsunny.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dsunny.activity.base.AppBaseActivity;
import com.dsunny.common.AppConstants;
import com.dsunny.database.LineInfoDao;
import com.dsunny.database.bean.LineInfo;
import com.dsunny.subway.R;
import com.infrastructure.activity.BaseActivity;

import java.util.List;

public class LineDetailActivity extends BaseActivity {

    private Toolbar tbLineDetail;
    private RecyclerView rvLineDetail;
    private List<LineInfo> lstLineInfo;
    private LineInfoDao mLineInfoDao;
    private LineDetailAdapter mAdapter;

    @Override
    protected void initVariables() {
        mLineInfoDao = new LineInfoDao();
        String lid = getIntent().getStringExtra(AppConstants.KEY_LINE_DETAIL);
        Log.e("---", lid);
        lstLineInfo = mLineInfoDao.getLineInfo(lid);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_line_detail);

        tbLineDetail = findAppViewById(R.id.tb_line_detail);
        setSupportActionBar(tbLineDetail);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("线路查看");
        }

        rvLineDetail = findAppViewById(R.id.rv_line_detail);
        mAdapter = new LineDetailAdapter(R.layout.item_line_info, lstLineInfo);
        mAdapter.isFirstOnly(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        rvLineDetail.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rvLineDetail.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class LineDetailAdapter extends BaseQuickAdapter<LineInfo, BaseViewHolder> {


        public LineDetailAdapter(int layoutResId, @Nullable List<LineInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LineInfo item) {
            helper.setText(R.id.tv_station_name, item.sname)
                    .setText(R.id.tv_station_time, item.stime + " - " + item.etime);
        }
    }

}
