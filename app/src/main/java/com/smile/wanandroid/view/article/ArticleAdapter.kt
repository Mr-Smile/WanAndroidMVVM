package com.smile.wanandroid.view.article

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smile.core.Play
import com.smile.core.util.setSafeListener
import com.smile.wanandroid.R
import com.smile.wanandroid.model.Article
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

class ArticleAdapter(mContext: Context, layoutId: Int, list: ArrayList<Article>) :
    CommonAdapter<Article>(mContext, layoutId, list) {

    override fun convert(holder: ViewHolder, t: Article, position: Int) {
        val articleLlItem = holder.getView<RelativeLayout>(R.id.articleLlItem)
        val articleIvImg = holder.getView<ImageView>(R.id.articleIvImg)
        val articleTvAuthor = holder.getView<TextView>(R.id.articleTvAuthor)
        val articleTvNew = holder.getView<TextView>(R.id.articleTvNew)
        val articleTvTop = holder.getView<TextView>(R.id.articleTvTop)
        val articleTvTime = holder.getView<TextView>(R.id.articleTvTime)
        val articleTvTitle = holder.getView<TextView>(R.id.articleTvTitle)
        val articleTvChapterName = holder.getView<TextView>(R.id.articleTvChapterName)
        val articleTvCollect = holder.getView<ImageView>(R.id.articleIvCollect)
        articleTvTitle.text = Html.fromHtml(t.title)
        articleTvChapterName.text = t.superChapterName
        articleTvAuthor.text = if (TextUtils.isEmpty(t.author)) t.shareUser else t.author
        articleTvTime.text = t.niceShareDate
        if (!TextUtils.isEmpty(t.envelopePic)) {
            articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(t.envelopePic).into(articleIvImg)
        }
        articleTvTop.visibility = if (t.type > 0) View.VISIBLE else View.GONE
        articleTvNew.visibility = if (t.fresh) View.VISIBLE else View.GONE
        if (t.collect) {
            articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        articleTvCollect.setSafeListener {
            if (Play.isLogin()) {
                if (!t.collect) {
                    articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                } else {
                    articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }
                setCollect(t.id, !t.collect)
            } else {
                // TODO: 2021/3/24 跳转登录页面
            }
        }

        articleLlItem.setOnClickListener {
            // TODO: 2021/3/24 跳转文章页面
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun setCollect(id: Int, b: Boolean) {

    }
}