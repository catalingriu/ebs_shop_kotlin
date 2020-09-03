package com.example.ebstestinternship

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

//object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FavEntry : BaseColumns {
        const val TABLE_NAME = "Favorite"
        const val COLUMN_NAME_TITLE = "item_id"
    }

    private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${FavEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FavEntry.COLUMN_NAME_TITLE} INTEGER)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FavEntry.TABLE_NAME}"

    class FavReaderDbHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "FavReader.db"
        }
    }


