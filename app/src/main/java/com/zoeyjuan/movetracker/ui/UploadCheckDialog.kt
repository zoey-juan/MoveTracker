package com.zoeyjuan.movetracker.ui

import android.content.Context
import android.support.v7.app.AlertDialog
import com.zoeyjuan.movetracker.R

class UploadCheckDialog {
    fun show(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(context.getString(R.string.dialog_msg))
        builder.setPositiveButton(android.R.string.yes, { dialog, whichButton ->
            (context as MainActivity).upload()
        })
        builder.setNegativeButton(android.R.string.no, { dialog, whichButton ->
            // do nothing
        })
        builder.create().show()
    }
}