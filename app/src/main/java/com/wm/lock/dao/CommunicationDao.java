package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.params.CommunicationQueryParam;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommunicationDao extends BaseDao<Communication, Long> {

    public CommunicationDao(Dao<Communication, Long> dao) {
        super(dao);
    }

    public Communication find(CommunicationQueryParam param) {
        try {
            final List<Communication> list = where(param).query();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private Where<Communication, Long> where(CommunicationQueryParam param) throws SQLException {
        final Where<Communication, Long> where = where(param.getIndex(), param.getLimit(), "id_", true);
        if (param.getType() != null) {
            where.and().eq("type", param.getType());
        }
        if (param.getSender() != null) {
            where.and().eq("sender", param.getSender());
        }
        return where;
    }

}
