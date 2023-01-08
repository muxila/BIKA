package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.databinding.ItemHistroyBinding
import com.muxiyun.bika.db.History
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey
import com.muxiyun.bika.utils.TimestampFormat

class HistoryAdapter :
    BaseBindingAdapter<History, ItemHistroyBinding>(R.layout.item_histroy) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: History,
        binding: ItemHistroyBinding,
        position: Int
    ) {
        GlideApp.with(holder.itemView)
            .load(GlideUrlNewKey(bean.fileServer, bean.path))
            .placeholder(R.drawable.placeholder_avatar_2)
            .into(binding.historyItemImage)
        binding.historyItemTime.text = TimestampFormat().getDateToString(bean.time)
    }
}