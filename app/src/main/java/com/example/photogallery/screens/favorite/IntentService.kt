package com.example.photogallery.screens.favorite

import android.content.Intent
import android.util.Log

@Suppress("DEPRECATION")
class IntentService(
    name: String = "IntentService"
) : android.app.IntentService(name) {

    companion object{
        const val DATA_NAME = "DATA"
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        if(intent != null){
            if(intent.getStringExtra(DATA_NAME) != null){
                //FIXME: Ахтунг опасно если пару раз выйти с экрана может крашнуть
//                while (true){
//                    Log.d("IntentService","${intent.getStringExtra(DATA_NAME)}")
//                }
            }
        }
    }
}