package com.muxiyun.bika.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muxiyun.bika.base.BaseViewModel
import com.muxiyun.bika.bean.KeywordsBean
import com.muxiyun.bika.db.Search
import com.muxiyun.bika.db.SearchRepository
import com.muxiyun.bika.network.RetrofitUtil
import com.muxiyun.bika.network.base.BaseHeaders
import com.muxiyun.bika.network.base.BaseObserver
import com.muxiyun.bika.network.base.BaseResponse

class SearchViewModel(application: Application) : BaseViewModel(application) {
    val liveDataSearchKey: MutableLiveData<BaseResponse<KeywordsBean>> by lazy {
        MutableLiveData<BaseResponse<KeywordsBean>>()
    }

    fun getKey() {
        val headers= BaseHeaders("keywords","GET").getHeaderMapAndToken()

        RetrofitUtil.service.keywordsGet(headers)
            .doOnSubscribe(this@SearchViewModel)
            .subscribe(object : BaseObserver<KeywordsBean>() {

                override fun onSuccess(baseResponse: BaseResponse<KeywordsBean>) {
                    liveDataSearchKey.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<KeywordsBean>) {
                    liveDataSearchKey.postValue(baseResponse)
                }
            })
    }

    private val searchRepository: SearchRepository = SearchRepository(application)
    val allSearchLive: LiveData<List<String>>
        get() = searchRepository.listLiveData

    fun insertSearch(vararg search: Search?) {
        searchRepository.insertSearch(*search)
    }

    fun deleteSearch(vararg search: Search?) {
        searchRepository.deleteSearch(*search)
    }

    fun deleteAllSearch() {
        searchRepository.deleteAllSearch()
    }


}