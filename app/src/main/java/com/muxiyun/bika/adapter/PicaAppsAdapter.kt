package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.PicaAppsBean
import com.muxiyun.bika.databinding.ItemPicaappsBinding
import com.muxiyun.bika.utils.GlideApp

class PicaAppsAdapter :
    BaseBindingAdapter<PicaAppsBean.App, ItemPicaappsBinding>(R.layout.item_picaapps) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: PicaAppsBean.App,
        binding: ItemPicaappsBinding,
        position: Int
    ) {

        GlideApp.with(holder.itemView)
            .load(bean.icon)
            .placeholder(R.drawable.placeholder_avatar_2)
            .into(binding.picaAppsImage)

    }
}