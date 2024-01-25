package com.cc221002.ccl3_tgf.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cc221002.ccl3_tgf.data.model.Category
import com.cc221002.ccl3_tgf.data.model.SingleEntry

// this creates the whole database with the two entities and creates the connection to the daos
@Database(entities = [SingleEntry::class, Category::class], version = 1)
abstract class EntriesDatabase : RoomDatabase() {
	abstract val dao:EntriesDao;
	abstract val categoriesDao: CategoriesDao
}