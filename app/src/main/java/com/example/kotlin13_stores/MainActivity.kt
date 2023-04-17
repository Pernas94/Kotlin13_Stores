 package com.example.kotlin13_stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin13_stores.databinding.ActivityMainBinding
import java.util.concurrent.LinkedBlockingQueue

 class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding:ActivityMainBinding
     private lateinit var mAdapter: StoreAdapter
     private lateinit var gridLayout : GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnSave.setOnClickListener {
            var storeEntity:StoreEntity = StoreEntity(name = mBinding.etName.text.toString().trim())

            Thread{
                StoreApplication.database.storeDao().addStore(storeEntity)
            }.start()

            mAdapter.add(storeEntity)
            mBinding.etName.text.clear()
        }
        initRecycler()


    }



     private fun getStores(){
         //variable especial que permite crea una cola de actividades de un tipo concreto.
         //Así podemos hacer que al entrar un valor a esa cola, se ejecute una acción en el MainThread de forma asíncrona
         val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()
         Thread{
             val stores = StoreApplication.database.storeDao().getAllStores()
             //Agregamos el valor a la cola
             queue.add(stores)

         }.start()

         //Con take() esperamos a que haya un cambio en la cola para realizar la acción.
         mAdapter.setStores(queue.take())

     }
     private fun initRecycler(){
         mAdapter = StoreAdapter(mutableListOf(), this)
         gridLayout = GridLayoutManager(this, 2)
         getStores()

         mBinding.recycler.apply {
            setHasFixedSize(true)
             layoutManager = gridLayout
             adapter = mAdapter
         }

     }

    override fun onClick(store: StoreEntity) {
        TODO("Not yet implemented")
    }

     override fun onFavouriteStore(storeEntity: StoreEntity) {
         storeEntity.isFavourite = !storeEntity.isFavourite
         val queue = LinkedBlockingQueue<StoreEntity>()
         Thread{
             StoreApplication.database.storeDao().updateStore(storeEntity)
             queue.add(storeEntity)
         }.start()

         mAdapter.update(queue.take())

     }


     override fun onDeleteStore(storeEntity: StoreEntity) {

         val queue = LinkedBlockingQueue<StoreEntity>()
         Thread{
             StoreApplication.database.storeDao().deleteStore(storeEntity)
             queue.add(storeEntity)
         }.start()

         mAdapter.delete(queue.take())
     }
 }