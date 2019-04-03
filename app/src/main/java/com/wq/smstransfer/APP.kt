package com.wq.smstransfer

import android.app.Application
import com.cheny.base.utils.SharePreHelper

/**
 * @author wq
 * @date 2019/4/3 下午3:13
 * @desc ${TODO}
 */
class APP :Application() {

    override fun onCreate() {
        super.onCreate()
        SharePreHelper.instance.initialize(this,"")
    }
}