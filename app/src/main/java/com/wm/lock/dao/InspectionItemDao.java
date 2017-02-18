package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InspectionItemDao extends BaseDao<InspectionItem, Long> {

    public InspectionItemDao(Dao<InspectionItem, Long> dao) {
        super(dao);
    }

    public List<String> listCategory(long inspectionId) {
        try {
            final List<InspectionItem> list = where("id_", true).and().eq("inspection_id", inspectionId).query();
            final List<String> result = new ArrayList<>();
            for (InspectionItem item : list) {
                if (!contains(result, item.getItem_cate_name())) {
                    result.add(item.getItem_cate_name());
                }
            }
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

    public List<LockDevice> listBluetoothByCategory(long inspectionId, String category) {
        final List<InspectionItem> list = listByCategory(inspectionId, category);
        return toBluetoothDeviceList(list);
    }

    public List<LockDevice> listBluetooth(long inspectionId) {
        final List<InspectionItem> list = list(inspectionId);
        return toBluetoothDeviceList(list);
    }

    public List<InspectionItem> list(long inspectionId) {
        try {
            return where("id_", true).and().eq("inspection_id", inspectionId).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private boolean contains(List<String> list, String input) {
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (TextUtils.equals(iterator.next(), input)) {
                return true;
            }
        }
        return false;
    }

    private List<LockDevice> toBluetoothDeviceList(List<InspectionItem> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        final List<LockDevice> result = new ArrayList<>();
        for (InspectionItem item : list) {
            final LockDevice bluetoothDevice = toBluetoothDevice(item);
            if (bluetoothDevice != null) {
                result.add(bluetoothDevice);
            }
        }
        return result;
    }

    private LockDevice toBluetoothDevice(InspectionItem inspectionItem) {
        if (TextUtils.isEmpty(inspectionItem.getCabinet_lock_mac())) {
            return null;
        }
        final LockDevice result = new LockDevice();
        result.setLock_mac(inspectionItem.getCabinet_lock_mac());
        result.setLock_name(inspectionItem.getCabinet_name());
        return result;
    }

}
