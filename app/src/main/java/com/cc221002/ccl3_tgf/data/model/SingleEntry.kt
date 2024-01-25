package com.cc221002.ccl3_tgf.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// this part is linking the categories table to the EntriesList table
@Entity(tableName = "EntriesList",
	foreignKeys = [ForeignKey(
		entity = Category::class,
		parentColumns = ["id"],
		childColumns = ["categoryId"],
		onDelete = ForeignKey.CASCADE // Choose appropriate action on deletion
	)]
)

// this part defines which information each single Entry has. Some are for displaying and some are just for sorting and organising
data class SingleEntry(
	val foodName: String?,
	val bbDate: String?,
	val categoryId: Int, // Reference to Category
	val portionAmount: String?,
	val portionType: String?,
	val isChecked: Int,
	val timeStampChecked: String,
	@PrimaryKey(autoGenerate = true) val id: Int = 0,

	)



