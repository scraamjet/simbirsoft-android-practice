package com.example.simbirsoft_android_practice.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.simbirsoft_android_practice.databinding.EditPhotoDialogBinding

class EditPhotoDialogFragment : DialogFragment() {
    private var _binding: EditPhotoDialogBinding? = null
    private val binding get() = _binding!!

    private var actionCallback: ((PhotoAction) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = EditPhotoDialogBinding.inflate(layoutInflater)
        return createDialog()
    }

    private fun createDialog(): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create().apply {
                initClickListeners()
            }
    }

    private fun initClickListeners() {
        binding.takePhoto.root.setOnClickListener {
            actionCallback?.invoke(PhotoAction.TAKE_PHOTO)
            dismiss()
        }

        binding.deletePhoto.root.setOnClickListener {
            actionCallback?.invoke(PhotoAction.DELETE_PHOTO)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(callback: (PhotoAction) -> Unit): EditPhotoDialogFragment {
            return EditPhotoDialogFragment().apply {
                actionCallback = callback
            }
        }
    }
}
