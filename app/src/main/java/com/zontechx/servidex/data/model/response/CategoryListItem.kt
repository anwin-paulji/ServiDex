package com.zontechx.servidex.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryListItem(

	@SerialName("categoryName")
	val categoryName: String? = null,

	@SerialName("isActive")
	val isActive: Boolean? = null,

	@SerialName("categoryIcon")
	val categoryUrl: String? = null,

	@SerialName("subCategories")
	val subCategory: List<SubCategoryItem> = emptyList(),

	@SerialName("_id")
	val id: String = "",

	var isSelected: Boolean = false
)

@Serializable
data class SubCategoryItem(
	@SerialName("isActive")
	val isActive: Boolean? = null,
	@SerialName("subCategoryName")
	val name: String? = null,
	@SerialName("_id")
	val id: String? = null,
	var isSelected: Boolean = false
)

