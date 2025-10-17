package com.zontechx.servidex.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ServiceData(
    val _id: String,
    val serviceTitle: String,
    val serviceDescription: String,
    val experienceLevel: String,
    val chargeBasis: ChargeBasis,
    val location: Location,
    val serviceProvider: ServiceProvider,
    val primaryCategory: Category,
    val categories: List<Category>,
    val subCategories: SubCategory,
    val createdAt: Long,
    val isEnabled: Boolean,
    val isVerified: Boolean,
    val serviceImages: List<ServiceImage>
)

@Serializable
data class ChargeBasis(
    val price: Int,
    val pricingType: String,
    val unitDuration: Int
)

@Serializable
data class Location(
    val lat: Double,
    val long: Double
)

@Serializable
data class ServiceProvider(
    val name: String,
    val userId: String,
    val mobileNumber: String
)

@Serializable
data class Category(
    val categoryName: String,
    val createdAt: Long,
    val isActive: Boolean,
    val categoryIcon: String,
    val categoryId: String
)

@Serializable
data class SubCategory(
    val subCategoryName: String,
    val categoryIds: List<String>,
    val createdAt: Long,
    val isActive: Boolean,
    val subCategoryId: String
)

@Serializable
data class ServiceImage(
    val originalUrl: String,
    val croppedUrl: String,
    val isPrimary: Boolean
)
