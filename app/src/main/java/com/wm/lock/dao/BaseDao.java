package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.entity.Inspection;

import java.sql.SQLException;

public abstract class BaseDao<T, ID> extends RuntimeExceptionDao<T, ID> {
    
    protected Dao<T, ID> dao;
    
    public BaseDao(Dao<T, ID> dao) {
        super(dao);
        this.dao = dao;
    }

    protected Where<T, ID> where() throws SQLException {
        return where(null, false);
    }

    protected Where<T, ID> where(long index, long limit) throws SQLException {
        return where(index, limit, null, false);
    }

    protected Where<T, ID> where(String orderByColumn, boolean asc) throws SQLException {
        return where(0, Integer.MAX_VALUE, orderByColumn, asc);
    }

    protected Where<T, ID> where(long index, long limit, String orderByColumn, boolean asc) throws SQLException {
        final QueryBuilder<T, ID> builder = dao.queryBuilder();
        if (!TextUtils.isEmpty(orderByColumn)) {
            builder.orderBy(orderByColumn, asc);
        }
        if (limit > 0 && limit != Integer.MAX_VALUE) {
            builder.offset(index * limit).limit(limit);
        }
        return builder.where().isNotNull("id_");
    }

}
