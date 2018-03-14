package com.dsunny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dsunny.common.AppConstants;
import com.dsunny.common.SubwayData;
import com.dsunny.subway.R;
import com.infrastructure.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 线路页面
 */
public class LineActivity extends BaseActivity {

    private List<String> lstLineName;
    private LineAdapter mLineAdapter;
    private RecyclerView rvLine;
    private Toolbar mTbLine;

    @Override
    protected void initVariables() {
        lstLineName = new ArrayList<>();
        lstLineName.addAll(Arrays.asList(SubwayData.LINE_NAMES_SEARSH));
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_line);

        mTbLine = findAppViewById(R.id.tb_line_search);
        setSupportActionBar(mTbLine);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("线路查看");
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        rvLine = findAppViewById(R.id.rv_line);
        mLineAdapter = new LineAdapter(R.layout.item_line,lstLineName);
        mLineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String lid = SubwayData.LINE_IDS_SEARSH[position];
                Intent intent = new Intent(LineActivity.this,LineDetailActivity.class);
                intent.putExtra(AppConstants.KEY_LINE_DETAIL, lid);
                startActivity(intent);
            }
        });

        mLineAdapter.isFirstOnly(false);
        mLineAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        rvLine.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL, false));
        rvLine.setAdapter(mLineAdapter);

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

    @Override
    protected void loadData() {

    }

    /**
     * 线路Adapter
     */
    static class LineAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


        public LineAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_line, item);
        }

    }

}
