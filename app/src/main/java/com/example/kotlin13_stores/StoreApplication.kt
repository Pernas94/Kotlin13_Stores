package com.example.kotlin13_stores

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class StoreApplication: Application() {

    //Hace el código dentro estático y accesible por todos.
    companion object{
        lateinit var database:StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()

        /*val MIGRATION_1_2 = object: Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE StoreEntity ADD COLUMN photoUrl TEXT NOT NULL DEFAULT ''")
            }

        }*/

        database = Room.databaseBuilder(this, StoreDatabase::class.java,
            "StoreDatabase").build()

        //addMigrations(MIGRATION_1_2)

    }
}