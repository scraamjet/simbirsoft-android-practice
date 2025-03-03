package com.example.simbirsoft_android_practice

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment

class EditPhotoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.edit_photo_dialog, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<LinearLayout>(R.id.take_photo)?.setOnClickListener {
            (parentFragment as? ProfileFragment)?.takePhotoFromCamera()
            dialog.dismiss()
        }

        dialogView.findViewById<LinearLayout>(R.id.delete_photo)?.setOnClickListener {
            (parentFragment as? ProfileFragment)?.profileImageView?.setImageResource(R.drawable.ic_launcher_background)
            dialog.dismiss()
        }

        return dialog
    }

    companion object {
        fun newInstance(): EditPhotoDialogFragment {
            return EditPhotoDialogFragment()
        }
    }
}