package com.think.linxuanxuan.ecos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.think.linxuanxuan.ecos.model.City;
import com.think.linxuanxuan.ecos.model.Contact;
import com.think.linxuanxuan.ecos.model.Province;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String TAG = "DatabaseHelper";
    // 数据库名称
    private static final String DATABASE_NAME = "Ecos.db";
    
    // 数据库version
    private static final int DATABASE_VERSION = 1;


    /*** 省数据操作Dao */
    private RuntimeExceptionDao<Province, String> mProvinceDao = null;

    /*** 城市数据操作Dao */
    private RuntimeExceptionDao<City, String> mCityDao = null;

    /*** 联系人数据操作Dao */
    private RuntimeExceptionDao<Contact, String> mContactDao = null;
    
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // 可以用配置文件来生成 数据表，有点繁琐，不喜欢用
       // super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * @param context
     * @param databaseName
     * @param factory
     * @param databaseVersion
     */
    public DatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion)
    {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource)
    {
    	Log.d(TAG, "oncreate database");
        try
        {
            TableUtils.createTable(connectionSource, Province.class);
            TableUtils.createTable(connectionSource, City.class);
        	TableUtils.createTable(connectionSource, Contact.class);
            
            
            //初始化DAO
            mProvinceDao = getProvinceDao();
            mCityDao = getCityDao();
        	mContactDao = getContactDao();
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
    }


    public RuntimeExceptionDao<Province, String> getProvinceDao()
    {
        if (mProvinceDao == null)
        {
            mProvinceDao = getRuntimeExceptionDao(Province.class);
        }
        return mProvinceDao;
    }

    public RuntimeExceptionDao<City, String> getCityDao()
    {
        if (mCityDao == null)
        {
            mCityDao = getRuntimeExceptionDao(City.class);
        }
        return mCityDao;
    }
    public RuntimeExceptionDao<Contact, String> getContactDao()
    {
        if (mContactDao == null)
        {
        	mContactDao = getRuntimeExceptionDao(Contact.class);
        }
        return mContactDao;
    }
   
    
    /**
     * 释放 DAO
     */
    @Override
    public void close() {
        super.close();
        mContactDao = null;
        mProvinceDao = null;
        mCityDao = null;
    }

}


