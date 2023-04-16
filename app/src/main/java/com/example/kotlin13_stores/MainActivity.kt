 package com.example.kotlin13_stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin13_stores.databinding.ActivityMainBinding

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

     private fun initRecycler(){
         mAdapter = StoreAdapter(mutableListOf(), this)
         gridLayout = GridLayoutManager(this, 2)
         mBinding.recycler.apply {
            setHasFixedSize(true)
             layoutManager = gridLayout
             adapter = mAdapter
         }
     }

    override fun onClick(store: StoreEntity) {
        TODO("Not yet implemented")
    }
}