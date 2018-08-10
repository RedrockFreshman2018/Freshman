package com.mredrock.cyxbs.freshman.utils.kt

import com.mredrock.cyxbs.freshman.utils.ToastUtils
import com.mredrock.cyxbs.freshman.utils.net.APIService
import com.mredrock.cyxbs.freshman.utils.net.HttpLoader.service
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 你们不用KT，这里我就不写文档了
 */

fun showToast(str: String) = ToastUtils.show(str)

fun <T> getBeanFromNet(success: (T) -> Unit, fail: (Throwable) -> Unit = { throw it }, observable: APIService.() -> Observable<T>) {
    observable(service)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(success, fail)
}