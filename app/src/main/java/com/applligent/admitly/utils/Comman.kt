package com.applligent.admitly.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.applligent.admitly.R

class Comman {
    companion object{

        fun showLongToast(context:Context,str:String){
            Toast.makeText(context,str,Toast.LENGTH_LONG).show()
        }

        fun showShortToast(context:Context,str:String){
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show()
        }
//
//        private  var mAlertDialog: AlertDialog?= null
//        fun showProgress(){
//            hideProgress()
//            if (mAlertDialog==null){
//                mAlertDialog= SpotsDialog.Builder()
//                    .setContext(this)
//                    .setTheme(R.style.CustomDialog)
//                    .build()
//            }
//            mAlertDialog?.show()
//        }
//
//        fun hideProgress(){
//            if(mAlertDialog!=null&&mAlertDialog!!.isShowing){
//                mAlertDialog?.dismiss()
//            }
//        }
    }
}