package com.muxiyun.bika.ui.leaderboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.muxiyun.bika.base.BaseViewModel
import com.muxiyun.bika.bean.ComicListBean2
import com.muxiyun.bika.network.RetrofitUtil
import com.muxiyun.bika.network.base.BaseHeaders
import com.muxiyun.bika.network.base.BaseObserver
import com.muxiyun.bika.network.base.BaseResponse

class LeaderboardDayViewModel(application: Application) : BaseViewModel(application) {
    var tt: String? =null
    val liveData: MutableLiveData<BaseResponse<ComicListBean2>> by lazy {
        MutableLiveData<BaseResponse<ComicListBean2>>()
    }


    fun getLeaderboard() {
        RetrofitUtil.service.leaderboardGet(tt.toString(), "VC", BaseHeaders("comics/leaderboard?tt=${tt}&ct=VC", "GET").getHeaderMapAndToken()
        )
            .doOnSubscribe(this@LeaderboardDayViewModel)
            .subscribe(object : BaseObserver<ComicListBean2>() {
                override fun onSuccess(baseResponse: BaseResponse<ComicListBean2>) {
                    // 请求成功
                    liveData.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<ComicListBean2>) {
                    liveData.postValue(baseResponse)
                }
            })
    }
}