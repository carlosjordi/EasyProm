package com.akipa.ui.mantenimiento_platos.gestion_platos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.akipa.R

class GestionPlatoDialog(
    private val message: String,
    private val onPositiveClick: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(message)
                setPositiveButton(getString(R.string.gestion_plato_dialog_positive_button)) { _: DialogInterface, _: Int ->
                    onPositiveClick()
                }
                setNegativeButton(getString(R.string.gestion_plato_dialog_negative_button)) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
            }.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}