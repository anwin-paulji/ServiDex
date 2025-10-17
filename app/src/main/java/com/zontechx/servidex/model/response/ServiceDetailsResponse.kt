package com.zontechx.servidex.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailsResponse(
    val createdAt: Long? = null,
    val experienceLevel: String? = null,
    val serviceProviderId: String? = null,
    val serviceTitle: String = "",
    val isVerified: Boolean? = null,
    val isEnabled: Boolean? = null,
    val serviceDescription: String? = null,
    val location: Location? = null,
    val _id: String,
    val subCategoryId: String? = null,
    val chargeBasis: ChargeBasis? = null,
    val categoryId: String? = null,
    val serviceProvider: ServiceProvider? = null,
    val primaryCategory: Category? = null,
    val categories: List<Category>? = null,
    val subCategories: SubCategory? = null,
    val serviceImages: List<ServiceImage>? = null
)
