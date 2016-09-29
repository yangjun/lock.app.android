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
        try {
            final long count = where().and().eq("room_name", input.getRoom_name()).countOf();
            if (count == 0) {
                create(input);
            }
            else {
                update(input);
            }
        } catch (SQLException e) {
            throw new DbException(e);
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
