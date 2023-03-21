package com.example.alarmmaps.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.alarmmaps.R
import com.example.alarmmaps.domain.entity.Alarm
import com.example.alarmmaps.presentation.MainActivity
import com.example.alarmmaps.presentation.MainViewModel

class SetAlarmDialog : DialogFragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var etName: EditText
    private lateinit var etRadius: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val name = arguments?.getString("name")
        val latitude = arguments?.getFloat("latitude")
        val longitude = arguments?.getFloat("longitude")
        val alarmID = arguments?.getInt("alarm_id")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.set_alarm_dialog, null)
            etName = view.findViewById(R.id.et_name)
            etRadius = view.findViewById(R.id.et_radius)
            if (name != null && name != "null") {
                etName.text = Editable.Factory.getInstance().newEditable(name)
            }
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons

                .setPositiveButton(R.string.set_new_alarm
                ) { dialog, id ->
                    if (alarmID != null) {
                        viewModel.editAlarm(
                            Alarm(
                                etName.text.toString(),
                                longitude!!,
                                latitude!!,
                                etRadius.text.toString().toFloat(),
                                true,
                                alarmID
                            )
                        )
                    } else {
                        viewModel.setAlarm(
                            Alarm(
                                etName.text.toString(),
                                longitude!!,
                                latitude!!,
                                etRadius.text.toString().toFloat(),
                                true
                            )
                        )
                    }
                    (activity as MainActivity).supportFragmentManager.popBackStack("List", 0)
                }
                .setNegativeButton(R.string.cancel
                ) { dialog, id ->
                    getDialog()?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}