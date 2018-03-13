package com.dsunny.database;

import com.dsunny.database.bean.LineInfo;

import java.util.List;

/**
 * Created by yusenDong on 2018/3/8.
 */

public class LineInfoDao extends BaseDao {

    public List<LineInfo> getLineInfo(String lid){

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM LINEINFO ");
        sql.append(" WHERE lid = '").append(lid).append("' ");

        return queryListBean(sql.toString(), LineInfo.class);
    }

}
