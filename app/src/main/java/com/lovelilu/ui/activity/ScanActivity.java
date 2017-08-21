package com.lovelilu.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dalong.scanqrcodelib.core.IViewFinder;
import com.dalong.scanqrcodelib.util.QRCodeDecoder;
import com.dalong.scanqrcodelib.view.ZXingScannerView;
import com.google.zxing.Result;
import com.lovelilu.R;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.utils.PermissionUtils;
import com.lovelilu.utils.SoundUtil;
import com.lovelilu.utils.UriUtils;
import com.lovelilu.widget.CustomViewFinderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ScanActivity extends SwipeBackActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.ll_light)
    LinearLayout ll_light;
    @BindView(R.id.ll_photo)
    LinearLayout ll_photo;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_flash)
    ImageView iv_flash;

    private ZXingScannerView mScannerView;
    //重置扫描时间
    private final int SCAN_TIME = 500;
    //是否播放声音
    private boolean isSound = true;
    //是否震动
    private boolean isVibrator = true;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        setTranslucent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
        } else {
            init();
        }
    }

    private void init() {
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
        if (isSound) {
            SoundUtil.initSoundPool(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScannerView != null) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        doQrCodeResult(rawResult.getText());

    }

    private void doQrCodeResult(String result) {
        //播放声音
        if (isSound) SoundUtil.play(1, 0);

        //震动
        if (isVibrator) showVibrator();


        Toast.makeText(this, "扫描结果：\n" + result, Toast.LENGTH_SHORT).show();

        /*Intent intent = new Intent(this, AppDetailActivity.class);
        intent.putExtra(Const.EXTRA_PACKAGE_NAME, result);
        startActivity(intent);*/

        finish();

        //重新启动扫描
        //resumeCameraPreview();
    }


    /**
     * 重新启动扫描
     */
    public void resumeCameraPreview() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, SCAN_TIME);
    }

    /**
     * 震动效果
     */
    private void showVibrator() {
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {30, 300};
        vibrator.vibrate(pattern, -1);
    }


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    init();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(ScanActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }


    @OnClick({R.id.ll_light, R.id.ll_photo, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_light:
                if (mScannerView != null) {
                    if (mScannerView.getFlash()) {
                        iv_flash.setImageResource(R.drawable.scan_flashlight_normal);
                    } else {
                        iv_flash.setImageResource(R.drawable.scan_flashlight_checked);
                    }
                    mScannerView.toggleFlash();
                }
                break;
            case R.id.ll_photo:
                doTakePhoto();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private final int INTENT_GET_PHOTO = 100;

    /**
     * 相册选择图片识别
     */
    public void doTakePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, INTENT_GET_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case INTENT_GET_PHOTO://相册选择回来的回调
                if (resultCode == Activity.RESULT_OK) {
                    if (null != data) {
                        Uri mImageCaptureUri = data.getData();
                        String imgagePath = UriUtils.getImageAbsolutePath(this, mImageCaptureUri);
                        if (TextUtils.isEmpty(imgagePath)) return;
                        final String result = QRCodeDecoder.syncDecodeQRCode(imgagePath);
//                        qrcodeTv.setText("识别内容为：\n" + result);
//                        Bitmap bitmap = BitmapFactory.decodeFile(imgagePath);
//                        qrcodeImg.setImageBitmap(bitmap);

                        //返回之后200MS之后开始跳转页面
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doQrCodeResult(result);
                            }
                        }, 200);
                    }
                }
                break;
        }
    }

}
