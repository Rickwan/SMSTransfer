package com.wq.smstransfer.net

/**
 * @author wq
 * @date 2019/3/28 下午6:54
 * @desc ${TODO}
 */
abstract class ResponseCallBack<T> {

    abstract fun onResponseCallback(t: T)

    abstract fun onFailureCallback()
}