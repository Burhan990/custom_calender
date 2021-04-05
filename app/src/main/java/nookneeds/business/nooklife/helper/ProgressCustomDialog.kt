package nookneeds.business.nooklife.helper

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import nookneeds.business.nooklife.R


class ProgressCustomDialog(context: Context) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.custom_loader)
        setCancelable(false)
    }
}