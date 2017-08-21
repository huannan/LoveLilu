package com.lovelilu.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.model.event.CompressImagesEvent;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.adapter.ImageGridAdapter;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.utils.BitmapUtils;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.PreferenceUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.utils.WindowHelper;
import com.lovelilu.widget.AppToolbar;
import com.lovelilu.widget.menu.SelectThemeDWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by huannan on 2016/8/24.
 */
public class AddDiaryActivity extends SwipeBackActivity implements RippleView.OnRippleCompleteListener {

    public static final String TAG = AddDiaryActivity.class.getSimpleName();

    @BindView(R.id.tv_theme)
    TextView tv_theme;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.et_title)
    EditText et_title;

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.grv_content)
    GridView grv_content;

    private List<String> images;
    private ImageGridAdapter mAdapter;

    @BindView(R.id.tv_weather)
    TextView tv_weather;

    @BindView(R.id.sv_content)
    ScrollView sv_content;

    private LoadingDialog mProgressDialog;
    private PopupWindow pop_menu;
    private View view_pop_menu;
    private RippleView rv_1;
    private RippleView rv_2;
    private File imageFile;

    private Diary d;
    private PopupWindow pop_weather;
    private View view_pop_weather;
    private RippleView rv_weather_1;
    private RippleView rv_weather_2;
    private RippleView rv_weather_3;
    private RippleView rv_weather_4;
    private Diary diary;
    private PopupWindow pop_theme;
    private View view_pop_theme;
    private RippleView rv_theme_1;
    private RippleView rv_theme_2;
    private RippleView rv_theme_3;

    @BindView(R.id.rl_candy)
    RelativeLayout rl_candy;
    @BindView(R.id.rl_arty)
    RelativeLayout rl_arty;
    @BindView(R.id.rl_antique)
    RelativeLayout rl_antique;
    @BindView(R.id.rl_default)
    RelativeLayout rl_default;
    @BindView(R.id.rl_love)
    RelativeLayout rl_love;

    private Integer curTheme = Constants.THEME.THEME_CANDY;
    private RippleView rv_theme_4;
    private String mSelectDate = "";
    private RippleView rv_theme_5;
    private SelectThemeDWindow mSelectThemeDWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
        init();
    }


    private void init() {

        setTranslucent();

        EventBus.getDefault().register(this);

        tv_date.setText(DayTimeUtils.getDate());

        et_title.setText(PreferenceUtils.getInstance(mCtx).getTitle());
        et_content.setText(PreferenceUtils.getInstance(mCtx).getContent());

        et_title.clearFocus();
        et_content.clearFocus();

//        Serializable s = getIntent().getSerializableExtra(Constants.KEY.DIARY);
//        if (s != null) {
//            diary = (Diary) s;
//        }

        initBar();
        initDialog();
        initMenu();

    }

    /**
     * 初始化菜单项
     */
    private void initMenu() {

        pop_menu = new PopupWindow(mCtx);

        view_pop_menu = getLayoutInflater().inflate(R.layout.pop_diary_menu, null);
        pop_menu.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_menu.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_menu.setBackgroundDrawable(new BitmapDrawable());
        pop_menu.setFocusable(true);
        pop_menu.setOutsideTouchable(false);
        pop_menu.setContentView(view_pop_menu);
        pop_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowHelper.clearBackgroundAlpha(AddDiaryActivity.this);
            }
        });

        rv_1 = (RippleView) view_pop_menu.findViewById(R.id.rv_1);
        rv_2 = (RippleView) view_pop_menu.findViewById(R.id.rv_2);

        rv_1.setOnRippleCompleteListener(this);
        rv_2.setOnRippleCompleteListener(this);


        pop_weather = new PopupWindow(mCtx);
        view_pop_weather = getLayoutInflater().inflate(R.layout.pop_diary_weather, null);
        pop_weather.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_weather.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_weather.setBackgroundDrawable(new BitmapDrawable());
        pop_weather.setFocusable(true);
        pop_weather.setOutsideTouchable(false);
        pop_weather.setContentView(view_pop_weather);
        pop_weather.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowHelper.clearBackgroundAlpha(AddDiaryActivity.this);
            }
        });

        rv_weather_1 = (RippleView) view_pop_weather.findViewById(R.id.rv_w_1);
        rv_weather_2 = (RippleView) view_pop_weather.findViewById(R.id.rv_w_2);
        rv_weather_3 = (RippleView) view_pop_weather.findViewById(R.id.rv_w_3);
        rv_weather_4 = (RippleView) view_pop_weather.findViewById(R.id.rv_w_4);

        rv_weather_1.setOnRippleCompleteListener(this);
        rv_weather_2.setOnRippleCompleteListener(this);
        rv_weather_3.setOnRippleCompleteListener(this);
        rv_weather_4.setOnRippleCompleteListener(this);


        pop_theme = new PopupWindow(mCtx);
        view_pop_theme = getLayoutInflater().inflate(R.layout.pop_theme, null);
        pop_theme.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_theme.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop_theme.setBackgroundDrawable(new BitmapDrawable());
        pop_theme.setFocusable(true);
        pop_theme.setOutsideTouchable(false);
        pop_theme.setContentView(view_pop_theme);
        pop_theme.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowHelper.clearBackgroundAlpha(AddDiaryActivity.this);
            }
        });

        rv_theme_1 = (RippleView) view_pop_theme.findViewById(R.id.rv_t_1);
        rv_theme_2 = (RippleView) view_pop_theme.findViewById(R.id.rv_t_2);
        rv_theme_3 = (RippleView) view_pop_theme.findViewById(R.id.rv_t_3);
        rv_theme_4 = (RippleView) view_pop_theme.findViewById(R.id.rv_t_4);
        rv_theme_5 = (RippleView) view_pop_theme.findViewById(R.id.rv_t_5);

        rv_theme_1.setOnRippleCompleteListener(this);
        rv_theme_2.setOnRippleCompleteListener(this);
        rv_theme_3.setOnRippleCompleteListener(this);
        rv_theme_4.setOnRippleCompleteListener(this);
        rv_theme_5.setOnRippleCompleteListener(this);
    }

    private void initDialog() {

        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_image_select));
        mProgressDialog.setCancelable(false);

    }

    private void initBar() {

        toolbar.setOnRightButtonClickListener(new AppToolbar.OnRightButtonClickListener() {
            @Override
            public void onClick() {
                //KeyBoardUtils.hideKeyBoard(mCtx);
                publicDiary();
            }
        });

        toolbar.setOnLeftButtonClickListener(new AppToolbar.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                //KeyBoardUtils.hideKeyBoard(mCtx);
                finish();
            }
        });

//        toolbar.setIvRight2Listener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                KeyBoardUtils.hideKeyBoard(mCtx);
//                pop_menu.showAsDropDown(toolbar.getIvRight2());
//                WindowHelper.setBackgroundAlpha(AddDiaryActivity.this, 0.15f);
//            }
//        });
    }

    private void publicDiary() {

        final String title = et_title.getText().toString();
        final String content = et_content.getText().toString();

        if (TextUtils.isEmpty(title)) {
            ToastUtils.showShort(mCtx, R.string.public_title_empty);
            return;
        }

        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(mCtx, R.string.public_content_empty);
            return;
        }

        mProgressDialog.show();

        if (imageFile == null) {
            d = new Diary(tv_date.getText().toString(), title, content, "", 0, tv_weather.getText().toString(), curTheme);
            d.setUserId(PreferenceUtils.getInstance(AddDiaryActivity.this).getUserId());

            publicDiary2(d);
        } else {
            final BmobFile file = new BmobFile(imageFile);
//            file.upload(new UploadFileListener() {
//                @Override
//                public void done(BmobException e) {
//                    d = new Diary(title, content, file.getFileUrl());
//                    publicDiary2(d);
//                }
//            });

            file.uploadblock(new UploadFileListener() {
                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);

                }

                @Override
                public void done(BmobException e) {
                    d = new Diary(tv_date.getText().toString(), title, content, file.getFileUrl(), 0, tv_weather.getText().toString(), curTheme);
                    d.setUserId(PreferenceUtils.getInstance(AddDiaryActivity.this).getUserId());

                    publicDiary2(d);
                }
            });
        }


    }

    private void publicDiary2(Diary d) {
        d.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //s是返回的ID
                    ToastUtils.showShort(mCtx, R.string.public_success);
                    EventBus.getDefault().post(new UpdateListEvent());

                    //清除历史
                    PreferenceUtils.getInstance(mCtx).setTitle("");
                    PreferenceUtils.getInstance(mCtx).setContent("");

                    finish();
                } else {
                    ToastUtils.showShort(mCtx, R.string.public_failed);
                }

                mProgressDialog.dismiss();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(CompressImagesEvent msg) {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.rv_1:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.CODE.REQUEST_CODE_CAMERA);//根据不同请求返回，这里=Camera_Request_Code=1时返回
                break;

            case R.id.rv_2:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image/*");
                intent1.putExtra("return-data", true);
                startActivityForResult(intent1, Constants.CODE.REQUEST_CODE_PICTURE);

                break;

            case R.id.rv_w_1:
                String weather_1 = getResources().getString(R.string.weather_1);
                tv_weather.setText(weather_1);
//                WeatherUtils.updateBg(tv_weather, weather_1);
                pop_weather.dismiss();
                break;

            case R.id.rv_w_2:
                String weather_2 = getResources().getString(R.string.weather_2);
                tv_weather.setText(weather_2);
//                WeatherUtils.updateBg(tv_weather, weather_2);
                pop_weather.dismiss();
                break;

            case R.id.rv_w_3:
                String weather_3 = getResources().getString(R.string.weather_3);
                tv_weather.setText(weather_3);
//                WeatherUtils.updateBg(tv_weather, weather_3);
                pop_weather.dismiss();
                break;

            case R.id.rv_w_4:
                String weather_4 = getResources().getString(R.string.weather_4);
                tv_weather.setText(weather_4);
//                WeatherUtils.updateBg(tv_weather, weather_4);
                pop_weather.dismiss();
                break;

            case R.id.rv_t_1:

                rl_candy.setVisibility(View.VISIBLE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                et_title.setTextColor(getResources().getColor(R.color.theme_candy_txt));
                et_content.setTextColor(getResources().getColor(R.color.theme_candy_txt));

                curTheme = Constants.THEME.THEME_CANDY;

                pop_theme.dismiss();

                tv_theme.setText(getResources().getString(R.string.candy));

                break;

            case R.id.rv_t_2:

                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.VISIBLE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                et_title.setTextColor(getResources().getColor(R.color.theme_arty_txt));
                et_content.setTextColor(getResources().getColor(R.color.theme_arty_txt));

                curTheme = Constants.THEME.THEME_ARTY;

                pop_theme.dismiss();

                tv_theme.setText(getResources().getString(R.string.arty));

                break;

            case R.id.rv_t_3:

                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.VISIBLE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                et_title.setTextColor(getResources().getColor(R.color.theme_antique_txt));
                et_content.setTextColor(getResources().getColor(R.color.theme_antique_txt));

                curTheme = Constants.THEME.THEME_ANTIQUE;

                pop_theme.dismiss();

                tv_theme.setText(getResources().getString(R.string.antique));

                break;

            case R.id.rv_t_4:

                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.VISIBLE);
                rl_love.setVisibility(View.GONE);

                et_title.setTextColor(getResources().getColor(R.color.white));
                et_content.setTextColor(getResources().getColor(R.color.white));

                curTheme = Constants.THEME.THEME_MON;

                pop_theme.dismiss();

                tv_theme.setText(getResources().getString(R.string.theme_mon));

                break;

            case R.id.rv_t_5:

                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.VISIBLE);

                et_title.setTextColor(getResources().getColor(R.color.white));
                et_content.setTextColor(getResources().getColor(R.color.white));

                curTheme = Constants.THEME.THEME_LOVE;

                pop_theme.dismiss();

                tv_theme.setText(getResources().getString(R.string.theme_love));

                break;

            default:
                break;
        }
        pop_menu.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CODE.REQUEST_CODE_CAMERA) {
            if (data == null) {
                return;
            } else {
                Bundle result = data.getExtras();
                if (result != null) {
                    Bitmap bm = result.getParcelable("data");//拿到数据存入bm

                    Object[] o = BitmapUtils.bitMapCompress(bm);
                    iv_image.setImageBitmap((Bitmap) o[0]);
                    imageFile = (File) o[1];
                }
            }
        }

        if (requestCode == Constants.CODE.REQUEST_CODE_PICTURE) {

            if (data == null) {
                return;
            } else {

//            Uri uri = data.getData();
//            try {
//                File file = new File(new URI(uri.toString()));
//                Object[] o = BitmapUtils.bitMapCompress(BitmapFactory.decodeFile(file.getAbsolutePath()));
//                iv_image.setImageBitmap((Bitmap) o[0]);
//                imageFile = (File) o[1];
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//                return;
//            }

                Bitmap bm = compressBitmap(null, null, this, data.getData(), 4, false);
                iv_image.setImageBitmap(bm);

//            String s = PictureUtil.savePicture(mCtx, bm);

                Object[] o = BitmapUtils.bitMapCompress(bm);
                iv_image.setImageBitmap((Bitmap) o[0]);
                imageFile = (File) o[1];

//            imageFile = new File(s);
            }
        }


//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            mSelectDate = data.getStringExtra(CalendarSelectorActivity.ORDER_DAY);
//
//            String[] info = mSelectDate.split("#");
//            Calendar c = Calendar.getInstance();
//            c.set(Integer.valueOf(info[0]), Integer.valueOf(info[1]) - 1, Integer.valueOf(info[2]));
//
//            tv_date.setText(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_WEEK) + "日" + " " + DayTimeUtils.getWeek(c.get(Calendar.DAY_OF_WEEK)));
//        }
    }

    /**
     * 图片压缩处理，size参数为压缩比，比如size为2，则压缩为1/4
     **/
    private Bitmap compressBitmap(String path, byte[] data, Context context, Uri uri, int size, boolean width) {
        BitmapFactory.Options options = null;
        if (size > 0) {
            BitmapFactory.Options info = new BitmapFactory.Options();
            /**如果设置true的时候，decode时候Bitmap返回的为数据将空*/
            info.inJustDecodeBounds = false;
            decodeBitmap(path, data, context, uri, info);
            int dim = info.outWidth;
            if (!width) dim = Math.max(dim, info.outHeight);
            options = new BitmapFactory.Options();
            /**把图片宽高读取放在Options里*/
            options.inSampleSize = size;
        }
        Bitmap bm = null;
        try {
            bm = decodeBitmap(path, data, context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


    /**
     * 把byte数据解析成图片
     */
    private Bitmap decodeBitmap(String path, byte[] data, Context context, Uri uri, BitmapFactory.Options options) {
        Bitmap result = null;
        if (path != null) {
            result = BitmapFactory.decodeFile(path, options);
        } else if (data != null) {
            result = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } else if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uri);
                result = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @OnClick(R.id.tv_weather)
    public void setWeather() {
        pop_weather.showAsDropDown(tv_weather);
        WindowHelper.setBackgroundAlpha(AddDiaryActivity.this, 0.15f);

//        KeyBoardUtils.hideKeyBoard(mCtx);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceUtils.getInstance(mCtx).setTitle(et_title.getText().toString().trim());
        PreferenceUtils.getInstance(mCtx).setContent(et_content.getText().toString().trim());

//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
    }


    @OnClick(R.id.tv_theme)
    public void openThemeSelectMenu() {
//        pop_theme.showAsDropDown(tv_theme);
//        WindowHelper.setBackgroundAlpha(AddDiaryActivity.this, 0.15f);

        showMoreWindow(findViewById(R.id.view));

    }

    private void showMoreWindow(View view) {
        if (null == mSelectThemeDWindow) {
            mSelectThemeDWindow = new SelectThemeDWindow(this);
            mSelectThemeDWindow.init();
        }

        mSelectThemeDWindow.showMoreWindow(view, 100, new SelectThemeDWindow.OnMenuClickListener() {
            @Override
            public void onClick(int id) {
                switch (id) {

                    case R.id.ll_candy:

                        rl_candy.setVisibility(View.VISIBLE);
                        rl_arty.setVisibility(View.GONE);
                        rl_antique.setVisibility(View.GONE);
                        rl_default.setVisibility(View.GONE);
                        rl_love.setVisibility(View.GONE);

                        et_title.setTextColor(getResources().getColor(R.color.theme_candy_txt));
                        et_content.setTextColor(getResources().getColor(R.color.theme_candy_txt));

                        curTheme = Constants.THEME.THEME_CANDY;

                        tv_theme.setText(getResources().getString(R.string.candy));

                        break;

                    case R.id.ll_arty:

                        rl_candy.setVisibility(View.GONE);
                        rl_arty.setVisibility(View.VISIBLE);
                        rl_antique.setVisibility(View.GONE);
                        rl_default.setVisibility(View.GONE);
                        rl_love.setVisibility(View.GONE);

                        et_title.setTextColor(getResources().getColor(R.color.theme_arty_txt));
                        et_content.setTextColor(getResources().getColor(R.color.theme_arty_txt));

                        curTheme = Constants.THEME.THEME_ARTY;

                        tv_theme.setText(getResources().getString(R.string.arty));

                        break;

                    case R.id.ll_antique:

                        rl_candy.setVisibility(View.GONE);
                        rl_arty.setVisibility(View.GONE);
                        rl_antique.setVisibility(View.VISIBLE);
                        rl_default.setVisibility(View.GONE);
                        rl_love.setVisibility(View.GONE);

                        et_title.setTextColor(getResources().getColor(R.color.theme_antique_txt));
                        et_content.setTextColor(getResources().getColor(R.color.theme_antique_txt));

                        curTheme = Constants.THEME.THEME_ANTIQUE;

                        tv_theme.setText(getResources().getString(R.string.antique));

                        break;

                    case R.id.ll_mon:

                        rl_candy.setVisibility(View.GONE);
                        rl_arty.setVisibility(View.GONE);
                        rl_antique.setVisibility(View.GONE);
                        rl_default.setVisibility(View.VISIBLE);
                        rl_love.setVisibility(View.GONE);

                        et_title.setTextColor(getResources().getColor(R.color.white));
                        et_content.setTextColor(getResources().getColor(R.color.white));

                        curTheme = Constants.THEME.THEME_MON;

                        tv_theme.setText(getResources().getString(R.string.theme_mon));

                        break;

                    case R.id.ll_love:

                        rl_candy.setVisibility(View.GONE);
                        rl_arty.setVisibility(View.GONE);
                        rl_antique.setVisibility(View.GONE);
                        rl_default.setVisibility(View.GONE);
                        rl_love.setVisibility(View.VISIBLE);

                        et_title.setTextColor(getResources().getColor(R.color.white));
                        et_content.setTextColor(getResources().getColor(R.color.white));

                        curTheme = Constants.THEME.THEME_LOVE;
                        tv_theme.setText(getResources().getString(R.string.theme_love));

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick(R.id.iv_image)
    public void addPhoto() {

        pop_menu.showAsDropDown(findViewById(R.id.iv_image));
//        WindowHelper.setBackgroundAlpha(AddDiaryActivity.this, 0.15f);

    }

    @OnClick(R.id.tv_date)
    public void openDateSelect() {

        final AlertDialog dialog = new AlertDialog.Builder(mCtx).create();
        dialog.show();
        DatePicker picker = new DatePicker(mCtx);

        if (TextUtils.isEmpty(mSelectDate)) {
            picker.setDate(DayTimeUtils.getYear(), DayTimeUtils.getMon());
        } else {
            String[] info = mSelectDate.split("-");
            picker.setDate(Integer.valueOf(info[0]), Integer.valueOf(info[1]));
        }


        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                mSelectDate = date;
                dialog.dismiss();
                String[] info = mSelectDate.split("-");
                Calendar c = Calendar.getInstance();
                c.set(Integer.valueOf(info[0]), Integer.valueOf(info[1]) - 1, Integer.valueOf(info[2]));
                tv_date.setText(info[0] + "年"
                        + info[1] + "月"
                        + info[2] + "日"
                        + " " + DayTimeUtils.getWeek(c.get(Calendar.DAY_OF_WEEK)));
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);

    }


}
