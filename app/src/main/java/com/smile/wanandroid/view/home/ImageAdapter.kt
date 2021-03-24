package com.smile.wanandroid.view.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smile.wanandroid.model.BannerBean
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(private val mContext: Context, mData: List<BannerBean>) :
    BannerAdapter<BannerBean, ImageAdapter.BannerViewHolder>(mData) {

    inner class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        // 必须设置match——parent这是viewpager2 强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerBean?,
        position: Int,
        size: Int
    ) {
        Glide.with(mContext).load(data?.imagePath).into(holder!!.imageView)
        holder.imageView.setOnClickListener {  }
    }

}