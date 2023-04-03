package com.example.crud.local

import android.content.Context
import androidx.room.RoomDatabase
import com.example.crud.model.dashboard.MenusItem
import androidx.room.Database
import androidx.room.Room
import com.example.crud.local.dao.DashboardDao

@Database(entities = [MenusItem::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
    companion object{
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context):MyDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}