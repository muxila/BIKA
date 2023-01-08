package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.ChatListBean
import com.muxiyun.bika.databinding.ItemChatlistBinding
import com.muxiyun.bika.utils.GlideApp

class ChatListAdapter :
    BaseBindingAdapter<ChatListBean.Chat, ItemChatlistBinding>(R.layout.item_chatlist) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ChatListBean.Chat,
        binding: ItemChatlistBinding,
        position: Int
    ) {

        GlideApp.with(holder.itemView)
            .load(bean.avatar)
            .placeholder(R.drawable.placeholder_avatar_2)
            .into(binding.chatListImage)

    }
}