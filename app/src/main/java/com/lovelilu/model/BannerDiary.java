package com.lovelilu.model;

import com.lovelilu.model.base.BaseModel;

/**
 * Created by huannan on 2016/8/25.
 */
public class BannerDiary extends BaseModel {

    private String imgUrl;

    public BannerDiary(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
