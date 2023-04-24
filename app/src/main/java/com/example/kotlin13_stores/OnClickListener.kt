package com.example.kotlin13_stores

interface OnClickListener {
    fun onClick(storeId:Long)

    fun onFavouriteStore(storeEntity: StoreEntity)

    fun onDeleteStore(storeEntity: StoreEntity)

}