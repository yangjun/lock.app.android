package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public abstract class BaseDao<T, ID> extends RuntimeExceptionDao<T, ID> {
    
    protected Dao<T, ID> dao;
    
    public BaseDao(Dao<T, ID> dao) {
        super(dao);
        this.dao = dao;
    }

}
