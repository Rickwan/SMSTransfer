package com.wq.smstransfer.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author wq
 * @date 2019/4/10 上午9:33
 * @desc ${TODO}
 */

@Entity(tableName ="PhoneCallRecord")
data class PhoneCallRecord(

    var address: String?,
    var name: String?,
    var date: Long?,
    var isFeedback:Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)