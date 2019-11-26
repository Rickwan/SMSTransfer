package com.wq.smstransfer.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wq.smstransfer.db.entity.PhoneCallRecord
import io.reactivex.Flowable
import retrofit2.http.Query

/**
 * @author wq
 * @date 2019/4/10 上午9:40
 * @desc ${TODO}
 */
@Dao
interface PhoneCallRecordDao {

//    @Query("SELECT * FROM PhoneCallRecord ORDER BY date DESC ,isFeedback ASC")
    @Query("SELECT * FROM PhoneCallRecord ORDER BY date DESC")
    fun getAll(): Flowable<List<PhoneCallRecord>>

    @Query("SELECT * FROM PhoneCallRecord")
    fun requeryAll(): List<PhoneCallRecord>

    @Insert
    fun insertAll(vararg records: PhoneCallRecord)

    @Delete
    fun delete(phoneCallRecord: PhoneCallRecord)
}