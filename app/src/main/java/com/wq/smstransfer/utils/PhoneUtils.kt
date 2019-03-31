package com.wq.smstransfer.utils

import android.content.Context
import android.provider.ContactsContract

class PhoneUtils {

    companion object {
        fun queryContact(context: Context, phoneNum: String): String {

            val projection =
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
            var contactName = ""
            val cr = context.contentResolver
            val pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = " + phoneNum,
                null, null
            )
            if (pCur!!.moveToFirst()) {
                contactName = pCur
                    .getString(
                        pCur
                            .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                    )
                pCur.close()
            }
            return contactName
        }

    }


}