package com.leewilson.movienights.ui.main.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_update_profile.*
import kotlinx.android.synthetic.main.fragment_update_profile.profileImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "UpdateProfile"

@AndroidEntryPoint
class UpdateProfileFragment : BaseMainFragment(R.layout.fragment_update_profile) {

    private val REQUEST_IMAGE_CAPTURE = 1

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var picasso: Picasso

    private var changedProfileImageUri: Uri? = null
    private var takenPhotoFileName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        subscribeObservers()
        addListeners()
        viewModel.setStateEvent(ProfileStateEvent.FetchUserData)
    }

    private fun addListeners() {
        changeProfilePicImageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureEvent = registerForActivityResult(TakePicture()) { savedToDisk ->
            if (savedToDisk) {
                val file = File(requireContext().filesDir, "${takenPhotoFileName}.jpg")
                Log.d(TAG, "Getting picture file from filesDir: file name: ${takenPhotoFileName}")
                if (file.exists()) {
                    showProgressBar(true)
                    /* It's going to be huge, so compress it */
                    lifecycleScope.launch(Dispatchers.Default) {
                        val compressedImageFile = Compressor.compress(requireContext(), file)
                        withContext(Dispatchers.Main.immediate) {
                            changedProfileImageUri = Uri.fromFile(compressedImageFile)
                            picasso.load(changedProfileImageUri)
                                .into(profileImageView)
                            showProgressBar(false)
                        }
                    }
                }
            } else {
                showSnackbar(requireContext().getString(R.string.generic_error_msg))
            }
        }
        takePictureEvent.launch(createImageFile())
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_${timeStamp}"
        takenPhotoFileName = fileName
        val storageDir = File("${requireContext().filesDir}/")
        val file = File(storageDir, "${fileName}.jpg")
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
        return uri
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_update_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_save_profile -> {
                viewModel.setStateEvent(
                    ProfileStateEvent.UpdateUserData(
                        displayName = updateProfileNameField.text.toString(),
                        email = updateProfileEmailField.text.toString(),
                        bio = updateProfileBioField.text.toString(),
                        imageUri = if (changedProfileImageUri == null) null else changedProfileImageUri.toString()
                    )
                )
                val file = File("${requireContext().filesDir}/$takenPhotoFileName.jpg")
                if (file.exists() && !file.isDirectory) file.delete()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { state ->
            showProgressBar(state.loading)
            hideFragmentRootView(state.loading)
            state.message?.let { event ->
                event.getContentIfNotHandled()?.let { message ->
                    showSnackbar(message)
                }
            }
            state.data?.let { event ->
                event.getContentIfNotHandled()?.let { viewState ->
                    updateProfileNameField.setText(viewState.displayName)
                    updateProfileEmailField.setText(viewState.email)
                    updateProfileBioField.setText(viewState.bio)
                    if(viewState.imageUri.isNotBlank()) {
                        picasso.load(viewState.imageUri)
                            .placeholder(R.drawable.default_profile_img)
                            .into(profileImageView)
                    }
                }
            }
        })
    }
}