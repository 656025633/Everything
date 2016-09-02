package com.example.greendao;

import android.app.Application;
import android.content.Context;

import me.itangqi.greendao.DaoMaster;
import me.itangqi.greendao.DaoSession;

/**
 * Created by zcm on 2016/9/2.
 */

public class HBApplication extends Application {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static DaoMaster getDaoMaster(Context context)
    {
        if (daoMaster == null)
        {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, HBConstant.HB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context)
    {
        if (daoSession == null)
        {
            if (daoMaster == null)
            {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
