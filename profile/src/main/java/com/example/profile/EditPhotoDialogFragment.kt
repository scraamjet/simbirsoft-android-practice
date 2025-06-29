package com.example.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.profile.databinding.EditPhotoDialogBinding

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
            EditPhotoDialogKeys.REQUEST_KEY,
            bundleOf(EditPhotoDialogKeys.ACTION_KEY to action.name),
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
