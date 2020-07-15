package com.example.testlocationapp.view.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SettingsAlertDialog : DialogFragment() {

    private var listener : DialogListener? = null

    companion object {
        fun newInstance(listener: DialogListener, title: String?, msg: String?): SettingsAlertDialog? {
            val frag = SettingsAlertDialog()
            val args = Bundle()
            args.putString("title", title)
            args.putString("message", msg)
            frag.setArguments(args)
            frag.listener = listener
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments!!.getString("title")
        val msg = arguments!!.getString("message")

        val alertDialogBuilder = activity?.let { AlertDialog.Builder(it) }
        alertDialogBuilder?.setTitle(title)
        alertDialogBuilder?.setMessage(msg)
        alertDialogBuilder?.setPositiveButton(
            "Settings"
        ) { dialog, which ->

            listener?.onClickSettings()
            dialog?.dismiss()
            dialog?.cancel()
        }
        return alertDialogBuilder?.create()!!
    }

    interface DialogListener {
        fun onClickSettings()
    }
}