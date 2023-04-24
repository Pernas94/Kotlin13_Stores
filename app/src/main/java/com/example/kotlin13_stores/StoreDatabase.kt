package com.example.kotlin13_stores

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(StoreEntity::class), version = 2)
abstract class StoreDatabase:RoomDatabase() {
    abstract fun storeDao():StoreDao

}
