package com.wq.smstransfer.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wq.smstransfer.db.entity.SMSMessage
import retrofit2.http.Query

/**
 * @author wq
 * @date 2019/4/10 上午9:40
 * @desc ${TODO}
 */
@Dao
interface SMSMessageDao {

    @Query("SELECT * FROM SMSMessage")
    fun getAll(): List<SMSMessage>


    @Insert
    fun insertAll(vararg smsMessage: SMSMessage)

    @Delete
    fun delete(smsMessage: SMSMessage)
}