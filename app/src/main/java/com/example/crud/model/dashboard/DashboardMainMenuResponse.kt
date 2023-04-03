package com.example.crud.model.dashboard

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crud.local.LocalTableName

data class DashboardMainMenuResponse(
	val menus: List<MenusItem>? = null
)
@Entity(tableName = LocalTableName.TBL_DB_MAIN_MENU)
data class MenusItem(
	@PrimaryKey(autoGenerate = true)
	val menuId: Int? = null,
	val menuTitle: String? = null,
	val menuImage: String? = null
)

