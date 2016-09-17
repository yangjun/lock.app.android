package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspectionDao extends BaseDao<Inspection, Long> {

    public InspectionDao(Dao<Inspection, Long> dao) {
        super(dao);
    }

    public Inspection findByPlanId(String userJobNumber, String planId) {
        try {
            final Map<String, Object> map = new HashMap<>();
            map.put("user_job_number", userJobNumber);
            map.put("plan_id", planId);
            final List<Inspection> list = queryForFieldValues(map);
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public List<Inspection> list(InspectionQueryParam param) {
        try {
            return where(param).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public long count(InspectionQueryParam param) {
        try {
            return where(param).countOf();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public int add(Inspection inspection) {
        try {
            final long count = where().and().eq("plan_id", inspection.getPlan_id())
                    .and().eq("user_job_number", inspection.getUser_job_number())
                    .countOf();
            if (count <= 0) {
                return dao.create(inspection);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
        return 0;
    }

    public void delete(String userJobNumber, String planId) {
        try {
            final DeleteBuilder<Inspection, Long> builder = dao.deleteBuilder();
            builder.where().eq("user_job_number", userJobNumber).and().eq("plan_id", planId);
            builder.delete();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private Where<Inspection, Long> where(InspectionQueryParam param) throws SQLException {
        final Where<Inspection, Long> where = where(param.getIndex(), param.getLimit(), "plan_date", true);
        if (param.getState() >= 0) {
            where.and().eq("state", param.getState());
        }
        if (!TextUtils.isEmpty(param.getUser_job_number())) {
            where.and().eq("user_job_number", param.getUser_job_number());
        }
        return where;
    }

}
