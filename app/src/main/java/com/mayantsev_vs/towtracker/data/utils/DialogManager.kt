package com.mayantsev_vs.towtracker.data.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.SaveDialogBinding
import com.mayantsev_vs.towtracker.data.db.TrackItem

object DialogManager {

    // function that asks if the location is turned on if the user on their phone has turned the location completely off
    fun showLocEnableDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(R.string.location_disabled)
        dialog.setMessage(context.getString(R.string.location_dialog_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.yes_button)) { _, _ ->
            listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.no_button)) { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    // function that asks for permission to run in the background
    fun showBackgroundPermissionDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(context.getString(R.string.background_mode_disabled))
        dialog.setMessage(context.getString(R.string.background_mode_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.go_to_settings)) { _, _ ->
            listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel)) { _, _ ->
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
            val velocity = "${item?.speed} ${context.getString(R.string.km_h)}"
            val distance = "${item?.distance} ${context.getString(R.string.km)}"

            tvTime.text = time
            tvSpeed.text = velocity
            tvDistance.text = distance

            btnSave.setOnClickListener {
                listener.onClick()
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
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