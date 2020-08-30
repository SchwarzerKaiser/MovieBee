package com.leewilson.movienights.ui.main.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.leewilson.movienights.R
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.profile.state.ProfileStateEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_update_profile.*
import kotlinx.android.synthetic.main.fragment_update_profile.profileImageView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileFragment : BaseMainFragment(R.layout.fragment_update_profile) {

    private val REQUEST_IMAGE_CAPTURE = 1

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var picasso: Picasso

    private var changedProfileImageUri: Uri? = null

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
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            // Save to file and get URI
            val file = createImageFile()
            changedProfileImageUri = Uri.fromFile(file)
            val stream = FileOutputStream(file)
            imageBitmap
                .compress(
                Bitmap.CompressFormat.PNG,
                100,
                stream
            )
            picasso.load(changedProfileImageUri)
                .rotate(270f)
                .into(profileImageView)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
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
                            .rotate(270f)
                            .into(profileImageView)
                    }
                }
            }
        })
    }
}