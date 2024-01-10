package com.cc221002.ccl3_tgf.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import kotlinx.coroutines.flow.Flow

// This interface is defined as Data Access Object (DAO)
@Dao
interface EntriesDao {
	// all the functions below are for working with the database
	// Insert, Update and Delete are Predefined and are ready to use
	// You just create a function which you can call to use the method

	@Insert
	suspend fun insertEntry(singleEntry: SingleEntry)

	@Update
	suspend fun updateEntry(singleEntry: SingleEntry)

	@Delete
	suspend fun deleteEntry(singleEntry: SingleEntry)

	// you are still able to define your own queries
	// this function gets all the information from the table
	@Query("SELECT * FROM EntriesList")
	fun getEntries(): Flow<List<SingleEntry>>
}