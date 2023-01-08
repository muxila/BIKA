package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.ChapterBean
import com.muxiyun.bika.databinding.ItemChapterBinding
import com.muxiyun.bika.utils.TimeUtil

class ChapterAdapter : BaseBindingAdapter<ChapterBean.Eps.Doc, ItemChapterBinding>(R.layout.item_chapter) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: ChapterBean.Eps.Doc,
        binding: ItemChapterBinding,
        position: Int
    ) {
        binding.chapterTitle.text = bean.title
        binding.chapterTime.text = TimeUtil.B(bean.updated_at)
    }
}