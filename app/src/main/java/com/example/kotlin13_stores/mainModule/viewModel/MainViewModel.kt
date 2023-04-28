package com.example.kotlin13_stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin13_stores.StoreApplication
import com.example.kotlin13_stores.common.entities.StoreEntity
import com.example.kotlin13_stores.mainModule.model.MainInteractor
import java.util.concurrent.LinkedBlockingQueue

class MainViewModel : ViewModel(){

    private var stores:MutableLiveData<List<StoreEntity>>
    private var interactor: MainInteractor

    init{
        interactor= MainInteractor()
        stores = MutableLiveData()
        loadStores()
    }

    fun getStores(): LiveData<List<StoreEntity>>{
        return stores
    }

    private fun loadStores(){
        interactor.getStoresCallback(object : MainInteractor.StoresCallback{
            override fun getStoresCallback(stores: MutableList<StoreEntity>) {
                this@MainViewModel.stores.value = stores
            }
        })
    }
}