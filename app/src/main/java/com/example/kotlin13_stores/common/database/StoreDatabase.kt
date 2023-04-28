package com.example.kotlin13_stores.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlin13_stores.common.database.StoreDao
import com.example.kotlin13_stores.common.entities.StoreEntity

@Database(entities = arrayOf(StoreEntity::class), version = 2)
abstract class StoreDatabase:RoomDatabase() {
    abstract fun storeDao(): StoreDao

}
