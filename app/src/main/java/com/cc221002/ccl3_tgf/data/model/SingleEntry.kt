package com.cc221002.ccl3_tgf.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.cc221002.ccl3_tgf.data.Category


@Entity(tableName = "EntriesList",
	foreignKeys = [ForeignKey(
		entity = Category::class,
		parentColumns = ["id"],
		childColumns = ["categoryId"],
		onDelete = ForeignKey.CASCADE // Choose appropriate action on deletion
	)]
)
data class SingleEntry(
	val foodName: String,
	val bbDate: String,
	val categoryId: Int, // Reference to Category
	val portionAmount: Int,
	val portionType: String,
	val isChecked: Int,
	@PrimaryKey(autoGenerate = true) val id: Int = 0,

	)



