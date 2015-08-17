package com.think.linxuanxuan.ecos.model;

/**
 * @author enlizhang
 * @ClassName: Image
 * @Description: 作品图片
 * @date 2015年7月26日 上午12:28:56
 */
public class Image {

    /**
     * 图片本地路径
     */
    public String imageLocalPath;

    /**
     * 图片url
     */
    public String originUrl;

    /**
     * 缩略图url
     */
    public String thumbUrl;

    /**
     * the type of the image including cover and details.
     */
    public ImageType type;

    public enum ImageType {
        coverImage("coverImage"),
        detailImage("detailImage");

        private String type;

        private ImageType(String type) {
            this.type = type;
        }
    }
}

