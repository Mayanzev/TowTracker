package com.mayantsev_vs.towtracker.data.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.databinding.RouteDialogBinding

object DialogManager {

    // function that asks if the location is turned on if the user on their phone has turned the location completely off
    fun showLocationEnableDialog(context: Context, listener: SimpleListener) {
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
    fun showBackgroundPermissionDialog(context: Context, listener: SimpleListener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle(context.getString(R.string.background_mode_disabled))
        dialog.setMessage(context.getString(R.string.background_mode_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.go_to_settings)) { _, _ ->
            listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel_button)) { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    // shows the dialog box for saving a route
    fun showRouteDialog(context: Context, item: TrackItem?, listener: SimpleListener) {
        val builder = AlertDialog.Builder(context)
        val binding = RouteDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(binding.root)
        val dialog = builder.create()
        binding.apply {
            val time = "${context.getString(R.string.time)} ${item?.time}"
            val distance = "${context.getString(R.string.distance)} ${item?.distance} ${context.getString(R.string.km)}"
            val price = "${context.getString(R.string.price)} ${item?.price} ${context.getString(R.string.currency_symbol)}"

            tvTime.text = time
            tvDistance.text = distance
            tvPrice.text = price

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

    // dialog for changing the price per kilometer of the route on the button
    fun showPriceDialog(context: Context, listener: PriceListener) {
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.hint = context.getString(R.string.enter_price_hint)

        builder.setTitle(context.getString(R.string.price_dialog_title))
            .setMessage(context.getString(R.string.price_dialog_message))
            .setView(input)
            .setPositiveButton(context.getString(R.string.ok_button)) { _, _ ->
                val price = input.text.toString()
                listener.onClick(price)
            }
            .setNegativeButton(context.getString(R.string.cancel_button)) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }


    interface SimpleListener {
        fun onClick()
    }

    interface PriceListener {
        fun onClick(price: String)
    }
}