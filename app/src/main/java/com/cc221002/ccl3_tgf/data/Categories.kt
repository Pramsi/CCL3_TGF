package com.cc221002.ccl3_tgf.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
	val categoryName: String,
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
)