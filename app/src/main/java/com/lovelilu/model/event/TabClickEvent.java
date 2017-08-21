package com.lovelilu.model.event;

/**
 * Created by huannan on 2016/8/25.
 */
public class TabClickEvent {

    private int tabIndex;

    public TabClickEvent(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}
