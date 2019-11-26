package com.wq.smstransfer.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.wq.smstransfer.db.dao.AppDatabase
import com.wq.smstransfer.db.entity.PhoneCallRecord
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_phone_call_record.*


/**
 * @author wq
 * @date 2019/4/10 下午12:52
 * @desc ${TODO}
 */
class PhoneCallRecordActivity : AppCompatActivity() {

    var adapter: PhoneCallRecordAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wq.smstransfer.R.layout.activity_phone_call_record)
        toolBar.setNavigationOnClickListener { finish() }

        queryData()
//        queryOnMainThread()

    }

    private fun initData(records: List<PhoneCallRecord>) {

        adapter = PhoneCallRecordAdapter(this, records)

        var layoutManager=LinearLayoutManager(this)

        layoutManager.orientation= RecyclerView.VERTICAL

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter= adapter

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, Color.GRAY)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun queryData() {

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "SMSTransfer"
        ).build()


        db.phoneCallRecordDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {

                for (record in it) {
                    Log.i("tag", "id:${record.id},address:${record.address},name:${record.name},date:${record.date}")
                }

                initData(it)

            }
    }

    private fun queryOnMainThread() {

        Log.i("tag", "---------------queryOnMainThread---------------")

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "SMSTransfer"
        ).allowMainThreadQueries().build()
        var records = db.phoneCallRecordDao().requeryAll()
        for (record in records) {
            Log.i("tag", "id:${record.id},address:${record.address},name:${record.name},date:${record.date}")
        }

        initData(records)
    }

}