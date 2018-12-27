package com.andro.whitelist.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andro.whitelist.R
import com.andro.whitelist.db.handler.WhitelistDatabase
import com.andro.whitelist.db.model.Contact
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_READ_PHONE_STATE = 1
    }

    private var contactAdapter: ContactAdapter? = null
    private lateinit var database: WhitelistDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bar)
        val actionbar: ActionBar? = supportActionBar
        actionbar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        database = WhitelistDatabase.getInstance(this)!!

        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, WhitelistActivity::class.java))
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS
                )
                requestPermissions(
                    permissions,
                    PERMISSION_REQUEST_READ_PHONE_STATE
                )
            }
        }

        contact_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        contact_recycler.setHasFixedSize(true)
        contactAdapter = ContactAdapter(this)
        contact_recycler.adapter = contactAdapter
        contactAdapter?.submitList(getContactsFromDatabase())
    }

    override fun onResume() {
        super.onResume()
        contactAdapter?.submitList(getContactsFromDatabase())
    }

    private fun getContactsFromDatabase(): List<Contact> {
        return database.whitelistDao().getWhitelists()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_PHONE_STATE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted: $PERMISSION_REQUEST_READ_PHONE_STATE", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        "Permission NOT granted: $PERMISSION_REQUEST_READ_PHONE_STATE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }
}
