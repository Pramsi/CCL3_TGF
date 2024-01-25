package com.cc221002.ccl3_tgf.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cc221002.ccl3_tgf.data.model.Category
import kotlinx.coroutines.flow.Flow

// this DAO is responsible for the Categories database table
@Dao
interface CategoriesDao {
	// this part inserts the sent category into the database
	@Insert
	suspend fun insertCategory(category: Category)

	// this part gets all the information from the databse
	@Query("SELECT * FROM Categories")
	fun getAllCategories(): Flow<List<Category>>
}
