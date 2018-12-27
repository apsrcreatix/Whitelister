package com.andro.whitelist.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andro.whitelist.R
import com.andro.whitelist.db.handler.WhitelistDatabase
import com.andro.whitelist.db.model.Contact
import kotlinx.android.synthetic.main.activity_whitelist.*


class WhitelistActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_READ_CONTACTS = 2
    }

    private var contactAdapter: ContactAdapter? = null
    private lateinit var database: WhitelistDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whitelist)

        back_button.setOnClickListener {
            onBackPressed()
        }

        database = WhitelistDatabase.getInstance(this)!!

        contact_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        contact_recycler.setHasFixedSize(true)
        contactAdapter = ContactAdapter(this, true)
        contact_recycler.adapter = contactAdapter
        contactAdapter?.previousSelectedList = getContactsFromDatabase()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(
                    Manifest.permission.READ_CONTACTS
                )
                requestPermissions(
                    permissions,
                    PERMISSION_REQUEST_READ_CONTACTS
                )
            } else {
                getContactList()
            }
        }
    }

    private fun getContactList() {
        val cr = contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val contactList = arrayListOf<Contact>()

        if (cur?.count ?: 0 > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val contact = Contact(id, name, phoneNo.replace("\\s".toRegex(), ""))
                        contactList.add(contact)
                    }
                    pCur.close()
                }
            }
        }
        contactAdapter?.submitList(contactList)
        cur?.close()
    }

    private fun getContactsFromDatabase(): List<Contact>? {
        val list = database.whitelistDao().getWhitelistsNonLive()
        return if (list.isEmpty()) null else list
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted: $PERMISSION_REQUEST_READ_CONTACTS", Toast.LENGTH_SHORT)
                        .show()
                    getContactList()
                } else {
                    Toast.makeText(
                        this,
                        "Permission NOT granted: $PERMISSION_REQUEST_READ_CONTACTS",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }
}
