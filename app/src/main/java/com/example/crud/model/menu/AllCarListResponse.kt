package com.example.crud.model.menu

data class AllCarListResponse(
	val carlist: List<CarListItem?>? = null
)

data class CarListItem(
	val carType: String? = null,
	val carName: String? = null,
	val carId: Int? = null,
	val carImage: String? = null
)

