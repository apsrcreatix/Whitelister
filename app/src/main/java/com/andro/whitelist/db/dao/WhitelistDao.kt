package com.andro.whitelist.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andro.whitelist.db.model.Contact

@Dao
interface WhitelistDao {

    /**
     * Insert a new whitelist detail
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: Contact)

    /**
     * Insert list of whitelist detail
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContacts(contacts: List<Contact>)

    /**
     * Gets all the whitelist
     */
    @Query("SELECT * FROM Contact")
    fun getWhitelists(): List<Contact>

    @Delete
    fun delete(contact: Contact)


}