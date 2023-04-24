package com.example.kotlin13_stores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.kotlin13_stores.databinding.ItemStoreBinding

class StoreAdapter(
    private var stores: MutableList<StoreEntity>,
    private var listener: OnClickListener
) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores.get(position)
        with(holder) {
            setListener(store)
            binding.tvName.text = store.name
            binding.cbFavourite.isChecked = store.isFavourite
            Glide.with(mContext).load(store.photoUrl).centerCrop().
            diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imagePhoto)

        }

    }

    fun add(storeEntity: StoreEntity) {

            if(!stores.contains(storeEntity)){
                stores.add(storeEntity)
                notifyItemInserted(stores.size-1)
            }

    }

    fun setStores(stores: MutableList<StoreEntity>) {

        this.stores = stores
        notifyDataSetChanged()
    }

    fun update(storeEntity: StoreEntity) {
        val position = stores.indexOf(storeEntity)
        if (position != -1) { //-1 implica que no se encontró
            stores.set(position, storeEntity)
            notifyItemChanged(position)
        }
    }


    fun delete(storeEntity: StoreEntity) {
        val position = stores.indexOf(storeEntity)
        if (position != -1) { //-1 implica que no se encontró
            stores.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)

        fun setListener(storeEntity: StoreEntity) {

            with(binding.root) {
                setOnClickListener {
                    listener.onClick(storeEntity.id)
                }

                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }

            }

            binding.cbFavourite.setOnClickListener {
                listener.onFavouriteStore(storeEntity)
            }


        }
    }
}