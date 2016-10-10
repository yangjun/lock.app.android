package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;

public class TemperatureHumidityDao extends BaseDao<TemperatureHumidity, Long> {

    public TemperatureHumidityDao(Dao<TemperatureHumidity, Long> dao) {
        super(dao);
    }

    public void sync(TemperatureHumidity input) {
        final TemperatureHumidity cacheItem = findByRoomName(input.getRoom_name());
        if (cacheItem == null) {
            create(input);
        }
        else {
            cacheItem.setHumidity(input.getHumidity());
            cacheItem.setTemperature(input.getTemperature());
            update(cacheItem);
        }
    }

    public TemperatureHumidity findByRoomName(String roomName) {
        try {
            final List<TemperatureHumidity> list = where().and().eq("room_name", roomName).query();
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
