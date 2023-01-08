package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.GamesBean
import com.muxiyun.bika.databinding.ItemGamesBinding
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey

class GamesAdapter :
    BaseBindingAdapter<GamesBean.Games.Docs, ItemGamesBinding>(R.layout.item_games) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: GamesBean.Games.Docs,
        binding: ItemGamesBinding,
        position: Int
    ) {
        GlideApp.with(holder.itemView)
            .load(GlideUrlNewKey(bean.icon.fileServer, bean.icon.path))
            .placeholder(R.drawable.placeholder_avatar_2)
            .into(binding.gamesItemImage)
    }
}