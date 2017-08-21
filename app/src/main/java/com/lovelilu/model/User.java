package com.lovelilu.model;

import com.lovelilu.model.base.BaseModel;

/**
 * Created by huannan on 2016/8/24.
 */
public class User extends BaseModel {

    private Integer userId;
    private String name;
    private String sign;//签名
    private Integer sex;//1--男，2--女
    private String avatarUrl;
    private String avatarBlurUrl;
    private String avatarBigUrl;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAvatarBigUrl() {
        return avatarBigUrl;
    }

    public void setAvatarBigUrl(String avatarBigUrl) {
        this.avatarBigUrl = avatarBigUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarBlurUrl() {
        return avatarBlurUrl;
    }

    public void setAvatarBlurUrl(String avatarBlurUrl) {
        this.avatarBlurUrl = avatarBlurUrl;
    }
}
