package com.think.linxuanxuan.ecos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author enlizhang
 * @ClassName: Province
 * @Description: TODO(省数据)
 * @date 2015年1月24日 下午2:03:47
 */
@DatabaseTable(tableName = "province_info")
public class Province {

    @DatabaseField(id = true)
    /*** 省id */
    public String provinceCode;

    @DatabaseField
    /*** 省名称 */
    public String provinceName;

    public Province() {

    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return provinceName;
    }
}
