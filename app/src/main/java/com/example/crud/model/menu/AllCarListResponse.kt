package com.example.crud.model.menu

data class AllCarListResponse(
	val carlist: List<CarlistItem?>? = null
)

data class CarlistItem(
	val carType: String? = null,
	val carName: String? = null,
	val carId: Int? = null,
	val carImage: String? = null
)

