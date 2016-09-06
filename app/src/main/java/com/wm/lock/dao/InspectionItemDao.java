package com.wm.lock.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InspectionItemDao extends BaseDao<InspectionItem, Long> {

    public InspectionItemDao(Dao<InspectionItem, Long> dao) {
        super(dao);
    }

    public List<String> listCategory(long inspectionId) {
        try {
            final List<InspectionItem> list = where("id_", true).and().eq("inspection_id", inspectionId).query();

            final Set<String> set = new HashSet<>();
            for (InspectionItem item : list) {
                set.add(item.getItem_cate_name());
            }

            final List<String> result = new ArrayList<>();
            result.addAll(result);
            return result;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public List<InspectionItem> listByCategory(long inspectionId, String category) {
        try {
            return where("id_", true).and().eq("inspection_id", inspectionId)
                    .and().eq("item_cate_name", category).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
