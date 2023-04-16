package com.example.kotlin13_stores

import android.app.Application
import androidx.room.Room

class StoreApplication: Application() {

    //Hace el código dentro estático y accesible por todos.
    companion object{
        lateinit var database:StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, StoreDatabase::class.java, "StoreDatabase").build()

    }
}