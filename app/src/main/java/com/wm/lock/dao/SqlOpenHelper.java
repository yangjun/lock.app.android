package com.wm.lock.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.wm.lock.exception.DbException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 */
public class SqlOpenHelper extends OrmLiteSqliteOpenHelper {

    private Context mCtx;

    private static final String DB_NAME = "db";

    private static final int DB_VERSION_INIT = 1;
    private static final int DB_VERSION_CURR = 1;

    private SqlParser mSqlParser;

    public SqlOpenHelper(Context context) {
        super(context, getPath(context), null, DB_VERSION_CURR);
        this.mCtx = context;
        this.mSqlParser = new SqlParser(context, "sql.xml");
    }

    private static String getPath(Context ctx) {
        return ctx.getExternalCacheDir().getAbsolutePath() + File.separator + DB_NAME; // FIXME，正式上线用下面的代码
//        return DB_NAME;
    }

    public SqlOpenHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        this.mCtx = context;
    }
    
    public SqlOpenHelper(Context context, String databaseName, CursorFactory factory,
                         int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
        this.mCtx = context;
    }
    
    public SqlOpenHelper(Context context, String databaseName, CursorFactory factory,
                         int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
        this.mCtx = context;
    }
    
    public SqlOpenHelper(Context context, String databaseName, CursorFactory factory,
                         int databaseVersion, InputStream is) {
        super(context, databaseName, factory, databaseVersion, is);
        this.mCtx = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db,
                         ConnectionSource connectionSource) {
        // 创建最初版本的数据库，如果需要升级则执行onUpgrade()
        upgrade(db, DB_VERSION_INIT);
        onUpgrade(db, DB_VERSION_INIT, DB_VERSION_CURR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            for (int i = oldVersion; i < newVersion; i++) {
                upgrade(db, i + 1);
            }
        }
    }

    private void upgrade(SQLiteDatabase db, int newVersion) throws DbException {
        try {
            final List<String> list = mSqlParser.parse(newVersion);
            for (String sql : list) {
                db.execSQL(sql);
            }
        }
        catch (Exception e) {
            throw new DbException(e);
        }
    }

}
