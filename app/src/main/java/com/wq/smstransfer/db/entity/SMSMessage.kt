package com.wq.smstransfer.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author wq
 * @date 2019/4/10 上午9:33
 * @desc ${TODO}
 */

@Entity(tableName = "SMSMessage")
data class SMSMessage(
    var address: String,
    var body: String,
    var date: Long,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0

)

