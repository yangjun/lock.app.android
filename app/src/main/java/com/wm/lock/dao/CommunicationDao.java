package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public Communication findNextWrite(String userJobNumber, long currId) {
        try {
            final List<Communication> list = where(0, 1, "id_", true)
                    .and().eq("source", userJobNumber)
                    .and().gt("id_", currId)
                    .query();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public void add(Communication communication) {
        try {
            final Map<String, Object> params = new HashMap<>();
            params.put("source", communication.getSource());
            params.put("target", communication.getTarget());
            params.put("directive", communication.getDirective());
            params.put("content", communication.getContent());

            final List<Communication> list = dao.queryForFieldValues(params);
            if (CollectionUtils.isEmpty(list)) {
                dao.create(communication);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public void delete(String userJobNumber, String bizId) {
        try {
            final DeleteBuilder<Communication, Long> builder = dao.deleteBuilder();
            builder.where().eq("id", bizId).and().eq("source", userJobNumber);
            builder.delete();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public Communication find(String userJobNumber, String bizId) {
        try {
            List<Communication> list = where()
                    .and().eq("id", bizId)
                    .and().eq("source", userJobNumber)
                    .query();
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private Where<Communication, Long> where(CommunicationQueryParam param) throws SQLException {
        final Where<Communication, Long> where = where(param.getIndex(), param.getLimit(), "id_", true);
        if (!TextUtils.isEmpty(param.getDirective())) {
            where.and().eq("directive", param.getDirective());
        }
        if (!TextUtils.isEmpty(param.getSource())) {
            where.and().eq("source", param.getSource());
        }
        return where;
    }

}
