package com.example.greendao;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import me.itangqi.greendao.DaoSession;
import me.itangqi.greendao.Note;
import me.itangqi.greendao.NoteDao;

/**
 * Created by zcm on 2016/9/2.
 * IM:1.插入功能
 *    2.删除功能
 *
 */

public class DBHelper
{
    private static Context mContext;
    private static DBHelper instance;

    public NoteDao noteDao;

    private DBHelper()
    {
    }

    public static DBHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DBHelper();
            if (mContext == null)
            {
                mContext = context;
            }

            // 数据库对象
            DaoSession daoSession = HBApplication.getDaoSession(mContext);
            instance.noteDao = daoSession.getNoteDao();
        }
        return instance;
    }

    /**增
     * 插入一条数据(不能插入集合)
     * @param item
     */
    public void addNote(Note item)
    {
        noteDao.insert(item);
    }

    /**删
     * 根据主键删除一条数据
     * @param id
     */
    public void deleteNote(long id){
        noteDao.deleteByKey(id);
    }

    /**
     *删除表中的所有的数据
     */
    public void deleteAllNote(){
        noteDao.deleteAll();
    }

    /**删
     * 根据自己定义的条件删除数据
     * @param sendId
     */
    public void deleteAnyNote(String sendId){
        QueryBuilder<Note> qb = noteDao.queryBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-10);
        Date date = new Date();
        Date date1 = calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yymmdd");
        Log.d("TAG","date1:"+formate.format(date1));
        Log.d("TAG","date:"+formate.format(date));
        qb.where(NoteDao.Properties.Date.between(date,date1));
        DeleteQuery<Note> dq = qb.buildDelete();
        dq.executeDeleteWithoutDetachingEntities();
    }

    /**查
     * 查询所有的数据
     * @return
     */
    public List<Note> getNoteList()
    {
        QueryBuilder<Note> qb = noteDao.queryBuilder();
        return qb.list();
    }

    /**查  根据条件查多少条
     * @param sendId
     * @return
     */
    public List<Note> getNoteBySender(String sendId){
        QueryBuilder qb = noteDao.queryBuilder();
        qb.where(NoteDao.Properties.SendId.eq(sendId));
        //todo 根据时间排序
        qb.orderAsc(NoteDao.Properties.Date);
        //查询从多少条到多少条
        qb.limit(1);
        qb.offset(10);
        return qb.list();
    }


    //查询根据日期
    public List<Note> queryByTime(){
        QueryBuilder<Note> qb = noteDao.queryBuilder();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-10);
        Date date1 = calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yymmdd");
        Log.d("TAG","date1:"+formate.format(date1));
        Log.d("TAG","date:"+formate.format(date));
        qb.where(NoteDao.Properties.Date.between(date,date1));
        return qb.list();
    }

}