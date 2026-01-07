package com.example.lifeos.ui.util

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

fun getPhoneNumberFromUri(context: Context, uri: Uri): String? {
    var number: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    if (cursor?.moveToFirst() == true) {
        val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
        val hasPhoneIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
        
        if (idIndex != -1 && hasPhoneIndex != -1) {
            val id = cursor.getString(idIndex)
            val hasPhone = cursor.getString(hasPhoneIndex)
            
            if (hasPhone == "1") {
                val phones = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,
                    null
                )
                if (phones?.moveToFirst() == true) {
                    val numberIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    if (numberIndex != -1) number = phones.getString(numberIndex)
                }
                phones?.close()
            }
        }
    }
    cursor?.close()
    return number
}

fun getContactDetailsFromUri(context: Context, uri: android.net.Uri): Pair<String, String>? {
    var details: Pair<String, String>? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    if (cursor?.moveToFirst() == true) {
        val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
        val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val hasPhoneIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

        if (idIndex != -1 && nameIndex != -1 && hasPhoneIndex != -1) {
            val id = cursor.getString(idIndex)
            val name = cursor.getString(nameIndex)
            val hasPhone = cursor.getString(hasPhoneIndex)

            if (hasPhone == "1") {
                val phones = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,
                    null
                )
                if (phones?.moveToFirst() == true) {
                    val numberIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    if (numberIndex != -1) {
                        val number = phones.getString(numberIndex)
                        details = Pair(number, name)
                    }
                }
                phones?.close()
            }
        }
    }
    cursor?.close()
    return details
}
