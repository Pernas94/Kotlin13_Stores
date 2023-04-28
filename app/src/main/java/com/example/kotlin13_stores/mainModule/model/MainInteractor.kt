package com.example.kotlin13_stores.mainModule.model

import com.example.kotlin13_stores.StoreApplication
import com.example.kotlin13_stores.common.entities.StoreEntity
import java.util.concurrent.LinkedBlockingQueue

class MainInteractor {

    interface StoresCallback{
        fun getStoresCallback(stores: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callback: StoresCallback){
        val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()

        Thread{
            val storeList = StoreApplication.database.storeDao().getAllStores()
            queue.add(storeList)
        }.start()

        callback.getStoresCallback(queue.take())
    }


}