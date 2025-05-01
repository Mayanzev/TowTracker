package com.mayantsev_vs.towtracker.main.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.RouteDialogBinding
import com.mayantsev_vs.towtracker.track.data.cache.TrackDBO

object DialogManager {

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

    fun showRouteDialog(context: Context, item: TrackDBO?, listener: SimpleListener) {
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

    fun showServiceDialog(context: Context, listener: ServiceListener) {
        val builder = AlertDialog.Builder(context)

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val serviceNameInput = EditText(context).apply {
            hint = context.getString(R.string.enter_service_name_hint)
        }

        val servicePriceInput = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = context.getString(R.string.enter_price_hint)
            filters = arrayOf(
                android.text.InputFilter { source, start, end, dest, dstart, dend ->
                    val result = dest.toString().substring(0, dstart) + source + dest.toString().substring(dend)
                    if (result.matches(Regex("^\\d{0,6}(\\.\\d{0,2})?\$"))) {
                        null
                    } else {
                        ""
                    }
                }
            )
        }

        layout.addView(serviceNameInput)
        layout.addView(servicePriceInput)

        val dialog = builder.setTitle(context.getString(R.string.add_service_dialog_title))
            .setMessage(context.getString(R.string.add_service_dialog_message))
            .setView(layout)
            .setPositiveButton(context.getString(R.string.ok_button), null)
            .setNegativeButton(context.getString(R.string.cancel_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val serviceName = serviceNameInput.text.toString().trim()
                val servicePrice = servicePriceInput.text.toString().trim()

                if (serviceName.isNotEmpty() && servicePrice.isNotEmpty()) {
                    listener.onClick(serviceName, servicePrice)
                    dialog.dismiss()
                } else {
                    if (serviceName.isEmpty()) {
                        context.showToast(context.getString(R.string.enter_service_name_hint))
                    }
                    if (servicePrice.isEmpty()) {
                        context.showToast(context.getString(R.string.enter_price_hint))
                    }
                }
            }
        }
        dialog.show()
    }




    interface SimpleListener {
        fun onClick()
    }

    interface PriceListener {
        fun onClick(price: String)
    }

    interface ServiceListener {
        fun onClick(serviceName: String, servicePrice: String)
    }
}