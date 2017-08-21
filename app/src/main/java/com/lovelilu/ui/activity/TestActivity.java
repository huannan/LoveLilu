package com.lovelilu.ui.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.ui.activity.base.BaseActivity;
import com.lovelilu.utils.PreferenceUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

public class TestActivity extends BaseActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "app-release.apk");

                BmobFile file = new BmobFile(f);

                file.uploadblock(new UploadFileListener() {
                    @Override
                    public void onProgress(Integer value) {
                        btn.setText("进度" + value);
                    }

                    @Override
                    public void done(BmobException e) {

                        btn.setText("上传成功");

                    }
                });

            }
        });
    }
}
