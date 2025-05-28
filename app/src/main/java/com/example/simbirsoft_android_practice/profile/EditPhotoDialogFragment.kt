package com.example.simbirsoft_android_practice.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.simbirsoft_android_practice.databinding.EditPhotoDialogBinding

class EditPhotoDialogFragment : DialogFragment() {
    private var _binding: EditPhotoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = EditPhotoDialogBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .apply { initClickListeners() }
    }

    private fun initClickListeners() {
        binding.choosePhoto.root.setOnClickListener {
            setResult(PhotoAction.CHOOSE_PHOTO)
        }

        binding.takePhoto.root.setOnClickListener {
            setResult(PhotoAction.TAKE_PHOTO)
        }

        binding.deletePhoto.root.setOnClickListener {
            setResult(PhotoAction.DELETE_PHOTO)
        }
    }

    private fun setResult(action: PhotoAction) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY_PHOTO_ACTION,
            bundleOf(KEY_PHOTO_ACTION to action.name)
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY_PHOTO_ACTION = "photo_action_request"
        const val KEY_PHOTO_ACTION = "photo_action"
    }
}

