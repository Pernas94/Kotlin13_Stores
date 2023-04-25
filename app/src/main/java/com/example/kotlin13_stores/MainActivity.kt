 package com.example.kotlin13_stores

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin13_stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
         gridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
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

         updateStore(queue.take())

     }


     override fun onDeleteStore(storeEntity: StoreEntity) {

         val items = resources.getStringArray(R.array.array_options_item)
         MaterialAlertDialogBuilder(this)
             .setTitle(R.string.dialog_options_title)
             .setItems(items) { _, i ->
                 when(i){
                     0-> confirmDelete(storeEntity)
                     1-> dial(storeEntity.phone)
                     2-> goToWebsite(storeEntity.website)
                 }
             }
             .show()

     }


     private fun confirmDelete(storeEntity: StoreEntity){
         MaterialAlertDialogBuilder(this)
             .setTitle(R.string.dialog_delete_title)
             .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                 val queue = LinkedBlockingQueue<StoreEntity>()
                 Thread {
                     StoreApplication.database.storeDao().deleteStore(storeEntity)
                     queue.add(storeEntity)
                 }.start()

                 mAdapter.delete(queue.take())
             }
             .setNegativeButton(R.string.dialog_delete_cancel, null)
             .show()
     }

     private fun dial(phone:String){
         val callIntent = Intent().apply{
             action = Intent.ACTION_DIAL
             data = Uri.parse("tel:$phone")
         }
         startIntent(callIntent)
     }

     private fun goToWebsite(website:String){

         if(website.isEmpty()) {
             Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_SHORT).show()
         }else{
             val websiteIntent = Intent().apply {
                 action = Intent.ACTION_VIEW
                 data = Uri.parse(website)
             }

             startIntent(websiteIntent)
         }
     }

     private fun startIntent(intent:Intent){
         if(intent.resolveActivity(packageManager)!=null){
             startActivity(intent)
         }else{
             Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_SHORT).show()
         }
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