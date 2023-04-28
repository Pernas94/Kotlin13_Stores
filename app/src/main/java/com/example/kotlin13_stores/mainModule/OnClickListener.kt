package com.example.kotlin13_stores.mainModule

import com.example.kotlin13_stores.common.entities.StoreEntity

interface OnClickListener {
    fun onClick(storeId:Long)

    fun onFavouriteStore(storeEntity: StoreEntity)

    fun onDeleteStore(storeEntity: StoreEntity)

}