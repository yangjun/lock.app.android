package com.wm.lock.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.exception.DbException;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;

import static com.tencent.bugly.crashreport.common.strategy.StrategyBean.d;

@EBean(scope = EBean.Scope.Singleton)
public class DaoManager {

    @RootContext
    Context mCtx;

    private SqlOpenHelper helper;

    private InspectionDao inspectionDao;
    private InspectionItemDao inspectionItemDao;
    private CommunicationDao communicationDao;
    private LockDeviceDao lockDeviceDao;
    private TemperatureHumidityDao temperatureHumidityDao;
    private AttachmentUploadDao attachmentUploadDao;

    @AfterInject
    void init() {
        helper = OpenHelperManager.getHelper(mCtx, SqlOpenHelper.class);
        try {
            inspectionDao = new InspectionDao((Dao<Inspection, Long>) helper.getDao(Inspection.class));
            inspectionItemDao = new InspectionItemDao((Dao<InspectionItem, Long>) helper.getDao(InspectionItem.class));
            communicationDao = new CommunicationDao((Dao<Communication, Long>) helper.getDao(Communication.class));
            lockDeviceDao = new LockDeviceDao((Dao<LockDevice, Long>) helper.getDao(LockDevice.class));
            temperatureHumidityDao = new TemperatureHumidityDao((Dao<TemperatureHumidity, Long>) helper.getDao(TemperatureHumidity.class));
            attachmentUploadDao = new AttachmentUploadDao((Dao<AttachmentUpload, Long>) helper.getDao(AttachmentUpload.class));
        } catch (SQLException e) {
            Logger.p("fail to instance dao", e);
            throw new DbException(e);
        }
    }

    private static class DaoManagerHolder {
        static final DaoManager instance = new DaoManager();
    }

    public static final DaoManager getInstance() {
        return DaoManagerHolder.instance;
    }

    public InspectionDao getInspectionDao() {
        return inspectionDao;
    }

    public InspectionItemDao getInspectionItemDao() {
        return inspectionItemDao;
    }

    public CommunicationDao getCommunicationDao() {
        return communicationDao;
    }

    public LockDeviceDao getLockDeviceDao() {
        return lockDeviceDao;
    }

    public TemperatureHumidityDao getTemperatureHumidityDao() {
        return temperatureHumidityDao;
    }

    public AttachmentUploadDao getAttachmentUploadDao() {
        return attachmentUploadDao;
    }

    public SqlOpenHelper getHelper() {
        return helper;
    }

    public void dropDb() {
        helper.drop();
    }

}
