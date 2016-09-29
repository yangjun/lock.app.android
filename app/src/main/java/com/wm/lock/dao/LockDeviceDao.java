package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class LockDeviceDao extends BaseDao<LockDevice, Long> {

    public LockDeviceDao(Dao<LockDevice, Long> dao) {
        super(dao);
    }

    public void sync(final String userJobNumber, final List<LockDevice> list) {
        doInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    final DeleteBuilder<LockDevice, Long> builder = dao.deleteBuilder();
                    builder.where().eq("user_job_number", userJobNumber);
                    builder.delete();
                    if (!CollectionUtils.isEmpty(list)) {
                        for (LockDevice item : list) {
                            item.setUser_job_number(userJobNumber);
                            create(item);
                        }
                    }
                } catch (SQLException e) {
                    throw new DbException(e);
                }
                return null;
            }
        });
    }

    public List<LockDevice> list(String userJobNumber) {
        try {
            return where().and().eq("user_job_number", userJobNumber).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
