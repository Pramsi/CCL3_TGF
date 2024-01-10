package com.cc221002.ccl3_tgf.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc221002.ccl3_tgf.data.model.SingleEntry

@Database(entities = [SingleEntry::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
	abstract val dao:EntriesDao;
}