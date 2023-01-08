package com.muxiyun.bika.adapter
import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.CategoriesBean
import com.muxiyun.bika.databinding.ItemCategoriesBinding
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey

//分类
class CategoriesAdapter :
    BaseBindingAdapter<CategoriesBean.Category, ItemCategoriesBinding>(R.layout.item_categories) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: CategoriesBean.Category,
        binding: ItemCategoriesBinding,
        position: Int
    ) {
        GlideApp.with(holder.itemView)
            .load(
                //判断是否是手动添加的数据
                if (bean.imageRes == null) {
                    //哔咔服务器问题 需自行修改图片请求路径
                    GlideUrlNewKey("https://s3.picacomic.com", bean.thumb.path)
                } else {
                    bean.imageRes
                }
            )
            .centerCrop()
            .placeholder(R.drawable.placeholder_transparent)
            .into(binding.categoriesItemImage)
    }

}