package com.lovelilu;

import android.os.Environment;

import java.io.File;

/**
 * Created by huannan on 2016/8/24.
 */
public class Constants {

    public static final class KEY {
        public static final String YEAR = "YEAR";
        public static final String MONTH = "MONTH";
        public static final String DAY = "DAY";
        public static final String DIARY = "DIARY";
        public static final String PHOTO = "PHOTO";
        public static final String TITLE = "TITLE";
        public static final String CONTENT = "CONTENT";
        public static final String IMAGE_URL = "IMAGE_URL";
        public static final String IMAGE_DESC = "IMAGE_DESC";
        public static final String USER_ID = "USER_ID";
        public static final String QUOTATION = "QUOTATION";
        public static final String ANNIVERSARY = "ANNIVERSARY";

        public static final int TAB_HOME = 0;
        public static final int TAB_DIARY = 1;
        public static final int TAB_ANN = 2;
        public static final int TAB_MINE = 3;

        public static final String JSON_LIST_DIARY = "JSON_LIST_DIARY";
        public static final String JSON_LIST_PHOTO = "JSON_LIST_PHOTO";
        public static final String JSON_LIST_ANN = "JSON_LIST_ANN";
    }

    public static final class USER_ID {

        public static final int LU_BAO_BAO = 0;
        public static final int NAN_BAO_BAO = 1;

    }


    public static final class ANN_TYPE {

        public static final int TYPE_DAY = 0;
        public static final int TYPE_QUOTATION = 1;

    }


    public static final class ACTION {
        public static final String UPDATE_ACTION = "com.nanmei.lovemilestonemin.appwidget.APP_UPDATE";
        public static final String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
        public static final String DATE_CHANGED = "android.intent.action.DATE_CHANGED";
        public static final String TIME_SET = "android.intent.action.TIME_SET";
    }

    public static final class FilePath {

        public static final String PICTURE_TEMP_PATH = Environment.getExternalStorageDirectory() + File.separator + "lubaobao" + File.separator + "picTemp" + File.separator;
    }

    public static final class CODE {
        public static final int REQUEST_CODE_CAMERA = 100;
        public static final int REQUEST_CODE_PICTURE = 101;
    }

    public static final class THEME {
        public static final int THEME_MON = 0;
        public static final int THEME_CANDY = 1;
        public static final int THEME_ARTY = 2;
        public static final int THEME_ANTIQUE = 3;
        public static final int THEME_LOVE = 4;

    }
}


