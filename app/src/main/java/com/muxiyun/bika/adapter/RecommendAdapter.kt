package com.muxiyun.bika.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muxiyun.bika.R
import com.muxiyun.bika.bean.RecommendBean
import com.muxiyun.bika.ui.comicinfo.ComicInfoActivity
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey

class RecommendAdapter(val context: Context) : RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    var datas= ArrayList<RecommendBean.Comic>()

    fun addNewData(data: List<RecommendBean.Comic>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, parent: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_recommend, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(datas[position])

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(context, ComicInfoActivity::class.java)
            intent.putExtra("id", datas[position]._id)
            intent.putExtra("fileserver", datas[position].thumb.fileServer)
            intent.putExtra("imageurl", datas[position].thumb.path)
            intent.putExtra("title", datas[position].title)
            intent.putExtra("author", datas[position].author)
            intent.putExtra("totalViews", "")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mImageView: ImageView
        var mTextView: TextView

        init {
            mImageView = view.findViewById(R.id.recommend_item_image)
            mTextView = view.findViewById(R.id.recommend_item_title)
        }

        fun setData(item: RecommendBean.Comic) {
            mTextView.text = item.title
            GlideApp.with(itemView)
                .load(GlideUrlNewKey(item.thumb.fileServer, item.thumb.path))
                .centerCrop()
                .placeholder(R.drawable.placeholder_transparent)
                .into(mImageView)
        }

    }

}