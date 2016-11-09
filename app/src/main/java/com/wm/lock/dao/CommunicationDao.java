package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.params.CommunicationDeleteParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunicationDao extends BaseDao<Communication, Long> {

    public CommunicationDao(Dao<Communication, Long> dao) {
        super(dao);
    }

    public Communication findNextWrite(String userJobNumber) {
        try {
            final List<Communication> list = where(0, 1, "create_date", true)
                    .and().eq("source", userJobNumber)
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

    public void delete(CommunicationDeleteParam param) {
        try {
            final DeleteBuilder<Communication, Long> builder = dao.deleteBuilder();
            final Where<Communication, Long> where = builder.where().gt("id_", 0);
            if (!TextUtils.isEmpty(param.getChatId())) {
                where.and().eq("id", param.getChatId());
            }
            if (!TextUtils.isEmpty(param.getSource())) {
                where.and().eq("source", param.getSource());
            }
            if (!TextUtils.isEmpty(param.getDirective())) {
                where.and().eq("directive", param.getDirective());
            }
            if (param.getContents() != null && param.getContents().length > 0) {
                for (String content : param.getContents()) {
                    where.and().like("content", "%" + content + "%");
                }
            }
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

}
