package com.lovelilu.model;

import android.content.Intent;

import com.lovelilu.model.base.BaseModel;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by huannan on 2016/8/24.
 */
public class Diary extends BaseModel {

    private String title;
    private String content;

    private String imageUrl;

    private Integer like;

    private String weather;

    private String date;

    private Integer theme;

    private Integer userId;

    public Diary() {
    }

    public Diary(String date, String title, String content, String imageUrl, Integer like, String weather, Integer theme) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.like = like;
        this.weather = weather;
        this.date = date;
        this.theme = theme;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
