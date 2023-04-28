package com.example.kotlin13_stores.common.utils

import com.example.kotlin13_stores.common.entities.StoreEntity

interface MainAux {
    fun hideFab(isVisible:Boolean=false)

    /**
     * Add store to RecyclerView Adapter
     * @param storeEntity StoreEntity
     */
    fun addStore(storeEntity: StoreEntity)

    /**
     * Update store in RecyclerView Adapter
     * @param storeEntity StoreEntity
     */
    fun updateStore(storeEntity: StoreEntity)

}