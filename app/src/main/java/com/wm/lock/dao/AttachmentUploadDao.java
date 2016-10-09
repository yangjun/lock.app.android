package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadSource;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.List;

public class AttachmentUploadDao extends BaseDao<AttachmentUpload, Long> {

    public AttachmentUploadDao(Dao<AttachmentUpload, Long> dao) {
        super(dao);
    }

    public List<AttachmentUpload> findNextGroup(String userJobNumber) {
        try {
            final List<AttachmentUpload> list = where(0 ,1, "id_", true).and().eq("user_job_number", userJobNumber)
                    .and().isNull("uploadedId")
                    .query();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }

            final AttachmentUpload item = list.get(0);
            return where("id_", true).and().eq("user_job_number", userJobNumber)
                    .and().eq("foreignId", item.getForeignId())
                    .and().eq("source", item.getSource())
                    .and().isNull("uploadedId")
                    .query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public void delete(String userJobNumber, AttachmentUploadSource source, long foreignId) {
        try {
            final DeleteBuilder<AttachmentUpload, Long> builder = dao.deleteBuilder();
            builder.where().eq("user_job_number", userJobNumber)
                    .and().eq("foreignId", foreignId)
                    .and().eq("source", source);
            builder.delete();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public AttachmentUpload findByPath(String path) {
        try {
            final List<AttachmentUpload> list = where(0 ,1, "id_", true).and().eq("path", path).query();
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
