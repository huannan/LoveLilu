package cn.aigestudio.datepicker.bizs.themes;

import android.graphics.Color;

/**
 * 主题的默认实现类
 * <p/>
 * The default implement of theme
 *
 * @author AigeStudio 2015-06-17
 */
public class DPBaseTheme extends DPTheme {
    @Override
    public int colorBG() {
        return 0xFFFFFFFF;
    }

    @Override
    public int colorBGCircle() {
        return Color.parseColor("#88FB8F7F");
    }

    @Override
    public int colorTitleBG() {
        return Color.parseColor("#FB8F7F");
    }

    @Override
    public int colorTitle() {
        return 0xEEFFFFFF;
    }

    @Override
    public int colorToday() {
        return Color.parseColor("#FB8F7F");
    }

    @Override
    public int colorG() {
        return 0xEE333333;
    }

    @Override
    public int colorF() {
        return 0xEEC08AA4;
    }

    @Override
    public int colorWeekend() {
        return 0xEEF78082;
    }

    @Override
    public int colorHoliday() {
        return 0x80FED6D6;
    }
}
