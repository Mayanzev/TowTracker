package com.mayantsev_vs.towtracker.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.SaveDialogBinding
import com.mayantsev_vs.towtracker.db.TrackItem

object DialogManager {
    fun showLocEnableDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.location_disabled)
        dialog.setMessage(context.getString(R.string.location_dialog_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
            listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showBackgroundPermissionDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.background_mode_disabled)
        dialog.setMessage(context.getString(R.string.background_mode_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Перейти в настройки") { _, _ ->
            listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showSaveDialog(context: Context, item: TrackItem?, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val binding = SaveDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {

            val time = "${item?.time}"
            val velocity = "${item?.velocity} km/h"
            val distance = "${item?.distance} km"

            tvTime.text = time
            tvSpeed.text = velocity
            tvDistance.text = distance

            bSave.setOnClickListener {
                listener.onClick()
                dialog.dismiss()
            }
            bCancel.setOnClickListener {
                dialog.dismiss()
            }

        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    interface Listener {
        fun onClick()
    }
}