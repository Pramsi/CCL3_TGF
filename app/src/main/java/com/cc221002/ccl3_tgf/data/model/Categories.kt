package com.cc221002.ccl3_tgf.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// this defines the Categories datatable. Here we only need an id and the name of the category
@Entity(tableName = "Categories")
data class Category(
	val categoryName: String,
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
)