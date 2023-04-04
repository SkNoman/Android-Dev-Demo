package com.example.crud.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crud.local.LocalTableName
import com.example.crud.model.dashboard.MenusItem

@Dao
interface DashboardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveDashboardMenus(menus: List<MenusItem>)

    @Query("SELECT * FROM tbl_db_main_menu ORDER BY menuId ASC")
    fun getAllDashboardMenuFromLocal():LiveData<List<MenusItem>>

    @Query("DELETE FROM ".plus(LocalTableName.TBL_DB_MAIN_MENU))
    suspend fun deleteAllDashboardMenuFromLocal()
}