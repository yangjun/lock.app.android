package com.wm.lock.module.biz;

import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.InspectionQueryParam;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by WM on 2015/8/6.
 */
@EBean
public class BizServiceJunit extends BizServiceBase {

    @Override
    public List<Inspection> listInspection(InspectionQueryParam param) {
        waitting();

        if (param.getState() != InspectionState.PENDING) {
            return super.listInspection(param);
        }

        final InspectionItem item1 = new InspectionItem();
        item1.setCabinet_lock_mac("21:32:4A:5C");
        item1.setCabinet_name("机柜1");
        item1.setEquipment_name("设备1");
        item1.setItem_cate_name("分类1");
        item1.setItem_id(UUID.randomUUID().toString());
        item1.setItem_name("检查冷却液的运行情况");

        final InspectionItem item2 = new InspectionItem();
        item2.setCabinet_lock_mac("2Q:32:4A:5A");
        item2.setCabinet_name("机柜2");
        item2.setEquipment_name("设备2");
        item2.setItem_cate_name("分类3");
        item2.setItem_id(UUID.randomUUID().toString());
        item2.setItem_name("检查机柜的温度，湿度，并把对应的结果友好圆满的展示出来");

        final Inspection inspection = new Inspection();
        inspection.setPlan_id(UUID.randomUUID().toString());
        inspection.setPlan_name("巡检计划001");
        inspection.setPlan_date(new Date());
        inspection.setLock_mac("12:3S:6R:7B");
        inspection.setRoom_name("机房1");
        inspection.setSend_man("张经理");

        final List<InspectionItem> inspectionItemList = new ArrayList<>();
        inspectionItemList.add(item1);
        inspectionItemList.add(item2);
        inspection.setInspection_item_list(inspectionItemList);

        final List<Inspection> result = new ArrayList<>();
        result.add(inspection);
        return result;
    }

    @Override
    public long countInspection(InspectionQueryParam param) {
        return listInspection(param).size();
    }

}
