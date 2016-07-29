package com.wm.lock.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class DaoManager {

    @RootContext
    Context mCtx;

    private SqlOpenHelper helper;
//    private PushMessageDao mPushMessageDao;

    @AfterInject
    void init() {
        helper = OpenHelperManager.getHelper(mCtx, SqlOpenHelper.class);
//        try {
//            mPushMessageDao = new PushMessageDao((Dao<PushMessage, String>) helper.getDao(PushMessage.class));
//        } catch (SQLException e) {
//            Logger.p("fail to instance dao", e);
//            throw new DbException(e);
//        }
    }

    private static class BizDaoManagerHolder {
        static final DaoManager instance = new DaoManager();
    }

    public static final DaoManager getInstance() {
        return BizDaoManagerHolder.instance;
    }

//    public PushMessageDao getPushMessageDao() {
//        return mPushMessageDao;
//    }

    public SqlOpenHelper getHelper() {
        return helper;
    }

//    public void resetAllTable() {
//    }
//
//    public <T> void resetTable(Class<T> clazz) {
//        try {
//            TableUtils.dropTable(helper.getConnectionSource(), clazz, false);
//            TableUtils.createTable(helper.getConnectionSource(), clazz);
//        }
//        catch (SQLException e) {
//            throw new DbException(e);
//        }
//    }

}
