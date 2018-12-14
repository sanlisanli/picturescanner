package me.mikasa.picturescanner.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import me.mikasa.picturescanner.R;
import me.mikasa.picturescanner.base.BaseRecyclerViewAdapter;
import me.mikasa.picturescanner.bean.GalleryItem;

public class GalleryAdapter extends BaseRecyclerViewAdapter<GalleryItem> {
    private Context mContext;
    public GalleryAdapter(Context context){
        this.mContext=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_gallery,parent,false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GalleryHolder)holder).bindView(mDataList.get(position));
    }

    class GalleryHolder extends BaseRvViewHolder{
        ImageView iv;
        GalleryHolder(View itemView){
            super(itemView);
            iv=itemView.findViewById(R.id.iv_gallery);//
        }

        @Override
        protected void bindView(GalleryItem galleryItem) {
            File file=new File(galleryItem.getImgUrl());
            Glide.with(mContext).load(file).asBitmap()
                    .placeholder(R.mipmap.ic_bili)
                    .error(R.mipmap.ic_bili)
                    .into(iv);
        }
    }
}
