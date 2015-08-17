package com.think.linxuanxuan.ecos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PhotoAibum类用于封存相册信息
 */
public class PhotoAibum implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;    // 相册名字
    private String count;   // 相册中图片的数量
    private String pathID;  // 相册第一张图片的路径作为相册的标示
    private int AibumID;    // 相册ID
    private List<PhotoItem> bitList = new ArrayList<PhotoItem>();    // 相册包含的照片

    public PhotoAibum() {
    }

    public PhotoAibum(String name, String count, String pathID) {
        super();
        this.name = name;
        this.count = count;
        this.pathID = pathID;
    }

    public List<PhotoItem> getBitList() {
        return bitList;
    }


    public void setBitList(List<PhotoItem> bitList) {
        this.bitList = bitList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPathID() {
        return pathID;
    }

    public void setPathID(String pathID) {
        this.pathID = pathID;
    }

    public int getAibumID() {
        return AibumID;
    }

    public void setAibumID(int aibumID) {
        AibumID = aibumID;
    }

    @Override
    public String toString() {
        return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
                + ", bitList=" + bitList + "]";
    }
}
