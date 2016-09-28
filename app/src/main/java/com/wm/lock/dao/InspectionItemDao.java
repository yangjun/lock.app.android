package com.wm.lock.dao;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.BluetoothDevice;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.exception.DbException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    public List<BluetoothDevice> listBluetoothByCategory(long inspectionId, String category) {
        final List<InspectionItem> list = listByCategory(inspectionId, category);
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

    private List<BluetoothDevice> toBluetoothDeviceList(List<InspectionItem> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        final List<BluetoothDevice> result = new ArrayList<>();
        for (InspectionItem item : list) {
            final BluetoothDevice bluetoothDevice = toBluetoothDevice(item);
            if (bluetoothDevice != null) {
                result.add(bluetoothDevice);
            }
        }
        return result;
    }

    private BluetoothDevice toBluetoothDevice(InspectionItem inspectionItem) {
        if (TextUtils.isEmpty(inspectionItem.getCabinet_lock_mac())) {
            return null;
        }
        final BluetoothDevice result = new BluetoothDevice();
        result.setMacAddress(inspectionItem.getCabinet_lock_mac());
        result.setName(inspectionItem.getCabinet_name());
        return result;
    }

}
