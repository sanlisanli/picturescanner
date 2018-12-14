package me.mikasa.picturescanner.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import me.mikasa.picturescanner.R;
import me.mikasa.picturescanner.base.BaseToolbarActivity;

public class ImageActivity extends BaseToolbarActivity {
    private String imgUrl;
    @Override
    protected int setLayoutResId() {
        return R.layout.activity_image;
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        imgUrl=intent.getStringExtra("imgurl");
    }

    @Override
    protected void initView() {
        PhotoView photoView=findViewById(R.id.photo_view);
        if (!TextUtils.isEmpty(imgUrl)){
            Glide.with(this).load(new File(imgUrl)).asBitmap().into(photoView);
        }
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
