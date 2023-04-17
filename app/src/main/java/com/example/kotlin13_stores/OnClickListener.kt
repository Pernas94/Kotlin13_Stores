package com.example.kotlin13_stores

interface OnClickListener {
    fun onClick(storeEntity:StoreEntity)

    fun onFavouriteStore(storeEntity: StoreEntity)

    fun onDeleteStore(storeEntity: StoreEntity)

}