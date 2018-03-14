package com.dsunny.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsunny.activity.base.AppBaseActivity;
import com.dsunny.activity.bean.TransferDetail;
import com.dsunny.activity.bean.TransferRoute;
import com.dsunny.activity.bean.TransferSubRoute;
import com.dsunny.common.AppConstants;
import com.dsunny.common.SubwayData;
import com.dsunny.subway.R;
import com.dsunny.util.AppUtil;
import com.dsunny.util.FormatUtil;
import com.dsunny.util.SubwayUtil;
import com.dsunny.util.DateUtil;
import com.infrastructure.activity.BaseActivity;
import com.infrastructure.util.LogUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 换乘详情
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int BLANK_VIEW_HEIGHT_DP = 10;

    private Date mNow;
    private int mTicketPrice;
    private TransferDetail mTransferDetail;

    private Toolbar mTbDetail;

    private LinearLayout llDetail;

    private Button btnTotal,btnTransfer, btnTime;

    @Override
    protected void initVariables() {
        mNow = new Date();
        mTransferDetail = (TransferDetail) getIntent().getSerializableExtra(AppConstants.KEY_TRANSFER_DETAIL);
        LogUtil.d("Test",mTransferDetail.toString());
        final TransferRoute tr = mTransferDetail.lstTransferRoute.get(0);
        mTicketPrice = SubwayUtil.getTransferPrice(tr.airportLineDistance, tr.otherLineDistance);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);

        btnTime = findAppViewById(R.id.btn_sort_time);
        btnTotal = findAppViewById(R.id.btn_sort_total);
        btnTransfer = findAppViewById(R.id.btn_sort_transfer);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSortTransferLine(SubwayData.TIME_SORT); // 进行排序
                //设置按钮样式
                btnTotal.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTotal.setTextColor(getResources().getColor(R.color.black));
                btnTime.setBackgroundResource(R.drawable.btn_sort_press);
                btnTime.setTextColor(getResources().getColor(R.color.white));
                btnTransfer.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTransfer.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSortTransferLine(SubwayData.TOTAL_SORT);
                btnTime.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTime.setTextColor(getResources().getColor(R.color.black));
                btnTotal.setBackgroundResource(R.drawable.btn_sort_press);
                btnTotal.setTextColor(getResources().getColor(R.color.white));
                btnTransfer.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTransfer.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSortTransferLine(SubwayData.TRANSFER_SORT);
                btnTotal.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTotal.setTextColor(getResources().getColor(R.color.black));
                btnTransfer.setBackgroundResource(R.drawable.btn_sort_press);
                btnTransfer.setTextColor(getResources().getColor(R.color.white));
                btnTime.setBackgroundResource(R.drawable.btn_sort_unpress);
                btnTime.setTextColor(getResources().getColor(R.color.black));

            }
        });

        mTbDetail = findAppViewById(R.id.tb_detail);
        setSupportActionBar(mTbDetail);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(getString(R.string.detail_title, mTransferDetail.fromStationName, mTransferDetail.toStationName));
        }

        llDetail = findAppViewById(R.id.ll_detail);

        addViews();
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

    /**
     * 排序
     *
     * @param keyword 排序关键字，根据什么排序
     */
    public void reSortTransferLine(int keyword) {
        delView();
        AppUtil.SubwayMapComp comp = new AppUtil.SubwayMapComp(keyword);
        AppUtil.SortSubwayMap sort = new AppUtil.SortSubwayMap(keyword);
        Collections.sort(mTransferDetail.lstTransferRoute, comp);
        addViews();
    }

    /**
     * 添加视图
     */
    private void addViews() {
        int routeIndex = 1;
        for (TransferRoute tr : mTransferDetail.lstTransferRoute) {
            llDetail.addView(createHeadView(routeIndex++, tr));
            llDetail.addView(createSpaceView());
            int subRouteIndex = 0;
            Date date = mNow;
            for (TransferSubRoute tsr : tr.lstTransferSubRoute) {
                if (subRouteIndex == 0) {
                    llDetail.addView(createStartView(tsr.fromStationName));
                } else {
                    llDetail.addView(createMiddleView(date, tsr.fromStationName));
                }
                View transferView = createTransferView(tsr);
                View stationView = createStationView(tsr.lstStationNames);
                transferView.findViewById(R.id.btn_expand).setTag(stationView);
                transferView.findViewById(R.id.btn_collapse).setTag(stationView);

                llDetail.addView(transferView);
                llDetail.addView(stationView);

                final int minute = tsr.lineName.equals(SubwayData.LINE_99) ? SubwayUtil.getAirportLineElapsedTime(tsr.distance) : SubwayUtil.getLineElapsedTime(tsr.distance);
                date = DateUtil.addMinute(date, minute + SubwayData.TRANSFER_MINUTE * subRouteIndex++);
            }
            llDetail.addView(createEndView(mTransferDetail.toStationName, tr));
            llDetail.addView(createSpaceView());
        }
    }

    private void delView() {
        llDetail.removeAllViews();
    }

    /**
     * 显示线路信息
     * @param index 线路n
     * @param tr    线路路径
     * @return head视图
     */
    private View createHeadView(int index, TransferRoute tr) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_detail_head, llDetail, false);
        TextView tvRouteIndex = findAppViewById(view, R.id.tv_route_index);
        TextView tvTimeframe = findAppViewById(view, R.id.tv_timeframe);
        TextView tvElapsedTime = findAppViewById(view, R.id.tv_minute);
        TextView tvTransferTimes = findAppViewById(view, R.id.tv_transfer_times);
        TextView tvPrice = findAppViewById(view, R.id.tv_price);
        TextView tvDistance = findAppViewById(view, R.id.tv_distance);

        tvRouteIndex.setText(getString(R.string.detail_route_index,index));
        tvTimeframe.setText(getString(R.string.detail_timeframe, DateUtil.getDateTime(mNow), DateUtil.getDateTime(DateUtil.addMinute(mNow, tr.elapsedTime))));
        tvElapsedTime.setText(getString(R.string.detail_minute, tr.elapsedTime));
        tvTransferTimes.setText(getString(R.string.detail_transfer_times, tr.lstTransferSubRoute.size() - 1));
        tvPrice.setText(getString(R.string.detail_price, mTicketPrice));
        tvDistance.setText(getString(R.string.detail_distance, FormatUtil.double1(1.0 * (tr.airportLineDistance + tr.otherLineDistance) / 1000)));

        return view;
    }

    /**
     * 显示起点车站
     * @param stationName 车站
     * @return start视图
     */
    private View createStartView(String stationName) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_detail_start, llDetail, false);
        TextView tvTimeline = findAppViewById(view, R.id.tv_timeline);
        TextView tvStation = findAppViewById(view, R.id.tv_station);

        tvTimeline.setText(DateUtil.getDateTime(mNow));
        tvStation.setText(stationName);

        return view;
    }

    /**
     * 显示中间车站
     * @param date        时间
     * @param stationName 车站
     * @return middle视图
     */
    private View createMiddleView(Date date, String stationName) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_detail_middle, llDetail, false);
        TextView tvTimeline = findAppViewById(view, R.id.tv_timeline);
        TextView tvStation = findAppViewById(view, R.id.tv_station);

        tvTimeline.setText(DateUtil.getDateTime(date));
        tvStation.setText(stationName);

        return view;
    }

    /**
     * 显示终点车站
     * @param stationName 车站
     * @param tr          线路路径
     * @return end视图
     */
    private View createEndView(String stationName, TransferRoute tr) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_detail_end, llDetail, false);
        TextView tvTimeline = findAppViewById(view, R.id.tv_timeline);
        TextView tvStation = findAppViewById(view, R.id.tv_station);

        tvTimeline.setText(DateUtil.getDateTime(DateUtil.addMinute(mNow, tr.elapsedTime)));
        tvStation.setText(stationName);

        return view;
    }

    /**
     * 显示换乘车站
     * @param tsr 换乘自路线
     * @return transfer视图
     */
    private View createTransferView(TransferSubRoute tsr) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_detail_transfer, llDetail, false);
        FrameLayout flTransfer = findAppViewById(view, R.id.fl_transfer);
        Button btnExpand = findAppViewById(view, R.id.btn_expand);
        Button btnCollapse = findAppViewById(view, R.id.btn_collapse);
        TextView tvTansfer = findAppViewById(view, R.id.tv_tansfer);

        if (tsr.lstStationNames.size() == 0) {
            flTransfer.setVisibility(View.INVISIBLE);
        } else {
            flTransfer.setVisibility(View.VISIBLE);

            btnExpand.setText(getString(R.string.detail_station_count, tsr.lstStationNames.size()));
            btnCollapse.setText(getString(R.string.detail_station_count, tsr.lstStationNames.size()));

            btnExpand.setOnClickListener(this);
            btnCollapse.setOnClickListener(this);
        }

        tvTansfer.setText(getString(R.string.detail_tansfer, tsr.lineName, tsr.direction));

        return view;
    }

    @Override
    public void onClick(View v) {
        View stationView = (View) v.getTag();
        FrameLayout flTransfer = (FrameLayout) v.getParent();
        //展开中间换乘车站View
        if (stationView.isShown()) {
            flTransfer.findViewById(R.id.btn_expand).setVisibility(View.VISIBLE);
            flTransfer.findViewById(R.id.btn_collapse).setVisibility(View.GONE);
            stationView.setVisibility(View.GONE);
        } else {
            flTransfer.findViewById(R.id.btn_expand).setVisibility(View.GONE);
            flTransfer.findViewById(R.id.btn_collapse).setVisibility(View.VISIBLE);
            stationView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示换乘中间车站 小视图
     * @param lstStationNames 车站名
     * @return station视图
     */
    private View createStationView(List<String> lstStationNames) {
        LinearLayout stationView = new LinearLayout(this);
        stationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        stationView.setOrientation(LinearLayout.VERTICAL);
        stationView.setVisibility(View.GONE);

        for (String stationName : lstStationNames) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_detail_station, llDetail, false);
            TextView rvStation = findAppViewById(view, R.id.tv_station);
            rvStation.setText(stationName);
            stationView.addView(view);
        }

        return stationView;
    }

    /**
     * @return 空白View
     */
    private View createSpaceView() {
        View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppUtil.dp2px(BLANK_VIEW_HEIGHT_DP)));
        return view;
    }

}
