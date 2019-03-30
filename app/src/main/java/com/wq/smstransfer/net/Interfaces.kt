package com.wq.smstransfer.net

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author wq
 * @date 2019/3/28 下午6:44
 * @desc ${TODO}
 */
interface Interfaces {


    @GET("{key}.send")
    fun sendMessage(
        @Path("key") key: String,
        @Query("text") text: String,
        @Query("desp") desp: String
    ): Observable<BaseModel>
}