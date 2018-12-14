package me.mikasa.picturescanner.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.mikasa.picturescanner.R;
import me.mikasa.picturescanner.adapter.GalleryAdapter;
import me.mikasa.picturescanner.base.BaseActivity;
import me.mikasa.picturescanner.bean.GalleryItem;
import me.mikasa.picturescanner.listener.OnRecyclerViewItemClickListener;
import me.mikasa.picturescanner.listener.PermissionListener;

public class GalleryActivity extends BaseActivity implements PermissionListener,
        OnRecyclerViewItemClickListener {
    private Context mContext;
    private static String galleryUrl;
    private static final int count=6;//循环添加图片数据的次数
    private List<GalleryItem>itemList=new ArrayList<>();
    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected int setLayoutResId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initData() {
        mContext=this;
        mAdapter=new GalleryAdapter(mContext);
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS};
        requestRuntimePermission(permissions,this);
    }

    @Override
    protected void initView() {
        mRecyclerView=findViewById(R.id.gallery_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));//setLayoutManager()
        mRecyclerView.setAdapter(mAdapter);//setAdapter()
    }

    @Override
    protected void initListener() {
        mAdapter.setOnRecyclerViewItemClickListener(this);//在recyclerview的adapter中设置点击监听事件
    }
    private List<GalleryItem>getGalleryItems(){
        List<GalleryItem>items;
        File galleryFile=new File(galleryUrl);
        if (galleryFile.exists()){
            File[] allFiles=galleryFile.listFiles();
            items=new ArrayList<>(allFiles.length);
            for (File file:allFiles){
                GalleryItem item=new GalleryItem();
                if (isImageFile(file.getPath())){
                    item.setImgUrl(file.getAbsolutePath());
                    items.add(item);
                }
           
            }
            return items;
        }else {
            return null;
        }
    }
    private void initFile(){
        //此部分纯属个人IO练习，可以忽略
        //从assets中读取图片资源
        AssetManager am=mContext.getAssets();
        List<String> bitmapPaths=new ArrayList<>();
        try {
            String[] paths=am.list("");
            for (String path:paths){
                if (path.endsWith(".jpg")||path.endsWith(".png")||path.endsWith(".jpeg")){
                    bitmapPaths.add(path);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //以字节流byte的形式将assets中的图片资源写入文件夹zzgallery
        try {
            File galleryDir=new File(galleryUrl);
            galleryDir.mkdir();
            for (String path:bitmapPaths){
                InputStream is=null;
                FileOutputStream fos=null;
                try {
                    String galleryName=String.valueOf(System.currentTimeMillis())+".jpeg";
                    try {//模拟延时
                        Thread.currentThread().sleep(50);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    fos=new FileOutputStream(galleryUrl+"/"+galleryName);
                    is=am.open(path);
                    Bitmap bitmap=BitmapFactory.decodeStream(is);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    fos.flush();
                    fos.close();
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if (is!=null){
                        is.close();
                    }
                    if (fos!=null){
                        fos.close();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //判断文件是不是图片文件
    private boolean isImageFile(String path){
        if (path.endsWith(".jpg")||path.endsWith(".png")||path.endsWith(".jpeg")||
                path.endsWith(".gif")||path.endsWith(".gif")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onGranted() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            galleryUrl=Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/zzgallery";//zzgallery就是想要自定义的文件夹
            File file=new File(galleryUrl);
            if (!file.exists()){
                initFile();//初始化图片文件夹
            }
        }
        //从指定文件夹中读取图片数据
        itemList=getGalleryItems();
        List<GalleryItem>list=new ArrayList<>();
        for (int i=0;i<count;i++){//循环添加图片数据
            list.addAll(itemList);
        }
        itemList.clear();
        itemList=list;
        mAdapter.updateData(itemList);//将数据与adapter绑定
    }

    @Override
    public void onDenied(List<String> deniedPermission) {
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent=new Intent(mContext,ImageActivity.class);
        intent.putExtra("imgurl",itemList.get(pos).getImgUrl());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(int pos) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_click:
                Toast.makeText(mContext,"点击图片可查看大图",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
