package com.dsunny.engine.interfaces;

import com.dsunny.activity.bean.TransferDetail;

/**
 * 地铁图抽象接口
 */
public interface ISubwayMap {
    //地图全部都有search方法，返回换乘信息
    TransferDetail search(final String fromStationName, final String toStationName);
}
