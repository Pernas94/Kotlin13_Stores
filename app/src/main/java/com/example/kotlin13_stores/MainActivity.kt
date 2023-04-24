 package com.example.kotlin13_stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin13_stores.databinding.ActivityMainBinding
import java.util.concurrent.LinkedBlockingQueue

 class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding:ActivityMainBinding
     private lateinit var mAdapter: StoreAdapter
     private lateinit var gridLayout : GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener {
            launchEditFragment()
        }
        initRecycler()


    }

     private fun launchEditFragment(args:Bundle?=null) {
         val fragment = EditStoreFragment()
         if(args!=null){
             fragment.arguments=args
         }
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()

         /*Al añadir un fragment, le tenemos que especificar 1º el view objetivo, 2º el fragment a inflar*/
         fragmentTransaction.add(R.id.containerMain, fragment)
         fragmentTransaction.addToBackStack(null)
         fragmentTransaction.commit() //Para que se apliquen los cambios

         hideFab(false)

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

    override fun onClick(storeId:Long) {

        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)

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


     /**
      * MAIN AUX
      * @param isVisible Boolean
      */
     override fun hideFab(isVisible: Boolean) {
         if(isVisible) mBinding.fab.show() else mBinding.fab.hide()
     }

     override fun addStore(storeEntity: StoreEntity) {
         mAdapter.add(storeEntity)
     }

     override fun updateStore(storeEntity: StoreEntity) {
         mAdapter.update(storeEntity)
     }
 }