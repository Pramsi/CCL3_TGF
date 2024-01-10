package com.cc221002.ccl3_tgf.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EntriesList")
data class SingleEntry(
	val foodName: String,
	val bbDate: String,
	val category: String,
	val portionAmount: Int,
	val portionType: String,
	val isChecked: Int,
	@PrimaryKey(autoGenerate = true) val id: Int = 0,

	)



