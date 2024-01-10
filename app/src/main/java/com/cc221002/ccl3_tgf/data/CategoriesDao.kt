package com.cc221002.ccl3_tgf.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoriesDao {
	@Insert
	suspend fun insertCategory(category: Category)
	@Query("SELECT * FROM Categories")
	fun getAllCategories(): Flow<List<Category>>

	// Add other necessary functions like getting a specific category, etc.
}
