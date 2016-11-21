package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.PinyinUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import static android.R.id.list;

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
                            insert(userJobNumber, item);
                        }
                    }
                } catch (SQLException e) {
                    throw new DbException(e);
                }
                return null;
            }
        });
    }

    public void insert(String userJobNumber, LockDevice lockDevice) {
        final String firstStr = lockDevice.getLock_name().substring(0, 1);
        lockDevice.setFirst_letter(PinyinUtils.getPinYinHeadChar(firstStr));
        lockDevice.setUser_job_number(userJobNumber);
        create(lockDevice);
    }

    public List<LockDevice> list(String userJobNumber) {
        try {
            return where().and().eq("user_job_number", userJobNumber).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public LockDevice findByMac(String userJobNumber, String macAddress) {
        try {
            final List<LockDevice> list = where(0, 1)
                    .and().eq("user_job_number", userJobNumber)
                    .and().eq("lock_mac", macAddress)
                    .query();
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
