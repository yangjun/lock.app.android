package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;

public class InspectionDao extends BaseDao<Inspection, Long> {

    public InspectionDao(Dao<Inspection, Long> dao) {
        super(dao);
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

    private Where<Inspection, Long> where(InspectionQueryParam param) throws SQLException {
        final Where<Inspection, Long> where = where(param.getIndex(), param.getLimit(), "plan_date", true);
        if (param.getState() >= 0) {
            where.and().eq("state", param.getState());
        }
        return where;
    }

}
