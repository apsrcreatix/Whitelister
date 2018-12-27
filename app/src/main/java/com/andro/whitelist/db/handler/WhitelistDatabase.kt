package com.andro.whitelist.db.handler

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andro.whitelist.db.dao.WhitelistDao
import com.andro.whitelist.db.model.Contact

@Database(entities = [Contact::class], version = 1)
abstract class WhitelistDatabase : RoomDatabase() {

    // Dao to handle the whitelist Table
    abstract fun whitelistDao(): WhitelistDao

    companion object {

        private const val DB_NAME = "whitelist.db"
        private var INSTANCE: WhitelistDatabase? = null

        fun getInstance(context: Context): WhitelistDatabase? {
            if (INSTANCE == null) {
                synchronized(WhitelistDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WhitelistDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun deleteAllTables() {
            INSTANCE?.clearAllTables()
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}