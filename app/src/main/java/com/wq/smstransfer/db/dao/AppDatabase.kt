package com.wq.smstransfer.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wq.smstransfer.db.entity.PhoneCallRecord
import com.wq.smstransfer.db.entity.SMSMessage

/**
 * @author wq
 * @date 2019/4/10 上午9:45
 * @desc ${TODO}
 */
@Database(entities = [SMSMessage::class, PhoneCallRecord::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun smsmessageDao(): SMSMessageDao

    abstract fun phoneCallRecordDao(): PhoneCallRecordDao
}