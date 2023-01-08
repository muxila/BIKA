package com.muxiyun.bika.ui.games

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.muxiyun.bika.base.BaseViewModel
import com.muxiyun.bika.bean.ActionBean
import com.muxiyun.bika.bean.GameInfoBean
import com.muxiyun.bika.network.RetrofitUtil
import com.muxiyun.bika.network.base.BaseHeaders
import com.muxiyun.bika.network.base.BaseObserver
import com.muxiyun.bika.network.base.BaseResponse

class GameInfoViewModel (application: Application) : BaseViewModel(application) {
    var gameId: String? = null//游戏id

    val liveData: MutableLiveData<BaseResponse<GameInfoBean>> by lazy {
        MutableLiveData<BaseResponse<GameInfoBean>>()
    }

    val liveData_like: MutableLiveData<BaseResponse<ActionBean>> by lazy {
        MutableLiveData<BaseResponse<ActionBean>>()
    }

    fun getGameInfo() {
        RetrofitUtil.service.gameInfoGet(
            gameId.toString(),
            BaseHeaders("games/${gameId}", "GET").getHeaderMapAndToken()
        )
            .doOnSubscribe(this)
            .subscribe(object : BaseObserver<GameInfoBean>() {
                override fun onSuccess(baseResponse: BaseResponse<GameInfoBean>) {
                    liveData.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<GameInfoBean>) {
                    liveData.postValue(baseResponse)
                }

            })
    }

    fun getLike() {
        RetrofitUtil.service.gameLikePost(
            gameId.toString(),
            BaseHeaders("games/${gameId}/like", "POST").getHeaderMapAndToken()
        )
            .doOnSubscribe(this)
            .subscribe(object : BaseObserver<ActionBean>() {
                override fun onSuccess(baseResponse: BaseResponse<ActionBean>) {
                    liveData_like.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<ActionBean>) {
                    liveData_like.postValue(baseResponse)
                }

            })
    }
}