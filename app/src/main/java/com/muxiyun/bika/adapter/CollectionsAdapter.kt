package com.muxiyun.bika.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muxiyun.bika.R
import com.muxiyun.bika.bean.CollectionsBean
import com.muxiyun.bika.widget.SpacesItemDecoration

class CollectionsAdapter (val context: Context) : RecyclerView.Adapter<CollectionsAdapter.ViewHolder>() {

    private var datas= ArrayList<CollectionsBean.Collection>()

    fun addNewData(data: List<CollectionsBean.Collection>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<CollectionsBean.Collection>) {
        val count = datas.size
        datas.addAll(datas.size, data)
        notifyItemRangeInserted(datas.size, data.size) //局部刷新
        notifyItemRangeChanged(datas.size, datas.size - count)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, parent: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_collections, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var mTextView: TextView
        private var text_null: TextView
        private var mRecyclerView: RecyclerView
        private var adapter: CollectionsItemAdapter

        init {
            mTextView = view.findViewById(R.id.item_collections_title)
            text_null = view.findViewById(R.id.item_collections_null)
            mRecyclerView = view.findViewById(R.id.item_collections_rv)
            adapter = CollectionsItemAdapter(context)
            val linearLayoutManager=LinearLayoutManager(context)
            linearLayoutManager.orientation=LinearLayoutManager.HORIZONTAL
            mRecyclerView.layoutManager = linearLayoutManager
            mRecyclerView.adapter = adapter

        }

        fun setData(item: CollectionsBean.Collection) {
            mTextView.text = item.title
            if (item.comics.isEmpty()) {
                text_null.visibility=View.VISIBLE
            }
            mRecyclerView.addItemDecoration(
                SpacesItemDecoration(
                    SpacesItemDecoration.px2dp(20F),
                    item.comics
                )
            )
            adapter.addNewData(item.comics)

        }

    }

}