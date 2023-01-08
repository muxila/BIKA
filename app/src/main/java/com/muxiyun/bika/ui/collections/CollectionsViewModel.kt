package com.muxiyun.bika.ui.collections

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.muxiyun.bika.base.BaseViewModel
import com.muxiyun.bika.bean.CollectionsBean
import com.muxiyun.bika.network.RetrofitUtil
import com.muxiyun.bika.network.base.BaseHeaders
import com.muxiyun.bika.network.base.BaseObserver
import com.muxiyun.bika.network.base.BaseResponse

class CollectionsViewModel(application: Application) : BaseViewModel(application) {
    val liveData: MutableLiveData<BaseResponse<CollectionsBean>> by lazy {
        MutableLiveData<BaseResponse<CollectionsBean>>()
    }

    fun getData() {
        val headers= BaseHeaders("collections","GET").getHeaderMapAndToken()
        RetrofitUtil.service.collectionsGet(headers)
            .doOnSubscribe(this@CollectionsViewModel)
            .subscribe(object : BaseObserver<CollectionsBean>() {

                override fun onComplete() {}
                override fun onSuccess(baseResponse: BaseResponse<CollectionsBean>) {
                    liveData.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<CollectionsBean>) {
                    liveData.postValue(baseResponse)
                }
            })
    }

}