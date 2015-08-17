package com.think.linxuanxuan.ecos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author enlizhang
 * @Title: City.java
 * @Description: 城市
 * @date 2015年7月25日 下午11:26:18
 */

@DatabaseTable(tableName = "city_info")
public class City {

    // 主键 id
    @DatabaseField(id = true)
    public String cityCode;

    @DatabaseField
    public String cityName;

    @DatabaseField(canBeNull = false)
    public String provinceId;

    public City() {

    }


    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getProvinceId() {
        return provinceId;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return cityName;
    }
}

