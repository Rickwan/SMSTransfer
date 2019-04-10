package com.wq.smstransfer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wq.smstransfer.db.entity.PhoneCallRecord
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author wq
 * @date 2019/4/10 下午1:02
 * @desc ${TODO}
 */
class PhoneCallRecordAdapter(context: Context) : RecyclerView.Adapter<PhoneCallRecordAdapter.ViewHolder>() {

    var mContext = context

    var mDatas: List<PhoneCallRecord>? = null

    constructor(context: Context, records: List<PhoneCallRecord>) : this(context) {
        this.mContext = context
        this.mDatas = records
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_phone_call_record, parent, false))
    }

    override fun getItemCount(): Int {

        return if (mDatas == null) {
            0
        } else {
            mDatas!!.size
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.init(mDatas!![position])

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var nameTv = itemView.findViewById<TextView>(R.id.nameTv)!!
        private var addressTv = itemView.findViewById<TextView>(R.id.addressTv)!!
        private var dateTv = itemView.findViewById<TextView>(R.id.dateTv)!!
        private var labelIv = itemView.findViewById<ImageView>(R.id.labelIv)!!

        fun init(record: PhoneCallRecord) {

            nameTv.text = "姓名：${record.name}"
            addressTv.text = "联系电话：${record.address}"

            var formatter = SimpleDateFormat("yyyy/MM/dd HH:mm")
            dateTv.text = "日期：${formatter.format(Date(record.date!!))}"

            labelIv.visibility = if (record.isFeedback) View.VISIBLE else View.INVISIBLE

        }
    }
}