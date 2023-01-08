package com.muxiyun.bika.adapter

import android.view.View
import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.ComicListBean
import com.muxiyun.bika.databinding.ItemComiclistBinding
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey

//漫画列表的第一种数据类型
class ComicListAdapter : BaseBindingAdapter<ComicListBean.Comics.Doc, ItemComiclistBinding>(R.layout.item_comiclist) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ComicListBean.Comics.Doc,
        binding: ItemComiclistBinding,
        position: Int
    ) {
        if (bean.likesCount != 0) {
            binding.comiclistItemLikeimage.visibility = View.VISIBLE
            binding.comiclistItemLiketext.visibility = View.VISIBLE
            binding.comiclistItemLiketext.text = bean.likesCount.toString()
        } else {
            binding.comiclistItemLikeimage.visibility = View.GONE
            binding.comiclistItemLiketext.visibility = View.GONE
        }
        if (bean.epsCount != 0) {
            if (bean.finished) {
                binding.comiclistItemBooktext.text =
                    "${bean.epsCount}C/${bean.pagesCount}P(完)"
            } else {
                binding.comiclistItemBooktext.text =
                    "${bean.epsCount}C/${bean.pagesCount}P"
            }
        } else {
            binding.comiclistItemBooktext.text = ""
        }


        if (bean.totalViews != 0) {
            binding.comiclistItemClicks.visibility = View.VISIBLE
            binding.comiclistItemClicks.text = "指数：${bean.totalViews}"
        } else {
            binding.comiclistItemClicks.visibility = View.GONE
        }


        var ategory = ""
        for (i in bean.categories) {
            ategory = "$ategory $i"
        }
        binding.comiclistItemCategory.text = "分类：$ategory"

        GlideApp.with(holder.itemView)
            .load(
                //哔咔服务器问题 需自行修改图片请求路径
                GlideUrlNewKey(
                    bean.thumb.fileServer, bean.thumb.path
                )
            )
            .centerCrop()
            .placeholder(R.drawable.placeholder_transparent)
            .into(binding.comiclistItemImage)
        holder.addOnClickListener(R.id.comiclist_item_image)

    }
}