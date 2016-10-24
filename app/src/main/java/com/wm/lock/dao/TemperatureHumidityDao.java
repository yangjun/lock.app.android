package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.entity.params.TemperatureHumidityQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;

public class TemperatureHumidityDao extends BaseDao<TemperatureHumidity, Long> {

    public TemperatureHumidityDao(Dao<TemperatureHumidity, Long> dao) {
        super(dao);
    }

    public void sync(TemperatureHumidity input) {
        final TemperatureHumidity cacheItem = findByRoomName(input.getRoom_name(), input.getUser_job_number());
        if (cacheItem == null) {
            create(input);
        }
        else {
            cacheItem.setHumidity(input.getHumidity());
            cacheItem.setTemperature(input.getTemperature());
            update(cacheItem);
        }
    }

    public List<TemperatureHumidity> list(TemperatureHumidityQueryParam param) {
        try {
            return where(param).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public TemperatureHumidity findByRoomName(String roomName, String userJobNumber) {
        try {
            final List<TemperatureHumidity> list = where().and().eq("room_name", roomName).
                    and().eq("user_job_number", userJobNumber)
                    .query();
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private Where<TemperatureHumidity, Long> where(TemperatureHumidityQueryParam param) throws SQLException {
        final Where<TemperatureHumidity, Long> where = where(param.getIndex(), param.getLimit(), "id_", true);
        where.and().eq("user_job_number", param.getUser_job_number());
        return where;
    }

}
