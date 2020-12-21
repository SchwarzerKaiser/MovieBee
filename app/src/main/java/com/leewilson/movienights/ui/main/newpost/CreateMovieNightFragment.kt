package com.leewilson.movienights.ui.main.newpost

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.leewilson.movienights.R
import com.leewilson.movienights.model.FollowUser
import com.leewilson.movienights.model.Movie
import com.leewilson.movienights.model.MovieDetail
import com.leewilson.movienights.model.MovieNight
import com.leewilson.movienights.ui.main.BaseMainFragment
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightStateEvent
import com.leewilson.movienights.ui.main.newpost.state.CreateMovieNightViewState
import com.leewilson.movienights.ui.selectguests.SelectGuestsActivity
import com.leewilson.movienights.ui.selectguests.SelectedGuestsAdapter
import com.leewilson.movienights.util.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_createmovienight.*
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

const val MOVIE_ARG = "com.leewilson.movienights.ui.main.newpost.MOVIE_ARG"
private const val TAG = "MovieNightFragment"

@AndroidEntryPoint
class CreateMovieNightFragment : BaseMainFragment(R.layout.fragment_createmovienight),
DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener {

    companion object {
        const val GUEST_UIDS = "com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment.GUEST_UIDS"
        const val MOVIENIGHT_DATE = "com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment.MOVIENIGHT_DATE"
        const val MOVIENIGHT_TIME = "com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment.MOVIENIGHT_TIME"
        const val MOVIE_DETAIL = "com.leewilson.movienights.ui.main.newpost.CreateMovieNightFragment.MOVIE_DETAIL"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: CreateMovieNightViewModel by viewModels()

    private var selectedGuestsAdapter: SelectedGuestsAdapter? = SelectedGuestsAdapter()

    private var specifiedDate: String? = null
    private var specifiedTime: String? = null
    private var specifiedMovie: MovieDetail? = null
    private var specifiedGuestUids: ArrayList<FollowUser> = ArrayList()

    private val startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val guests = intent.getParcelableArrayListExtra<FollowUser>(
                        SelectGuestsActivity.EXTRA_SELECTED_GUESTS
                    )
                guests?.let {
                    selectedGuestsAdapter?.submitList(it)
                    specifiedGuestUids = it
                }
            }
        }
    }

    private val picasso: Picasso by lazy {
        Picasso.get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideFragmentRootView(true)
        arguments?.let {
            val movie = it.getParcelable<Movie>(MOVIE_ARG)
            movie?.let { thismovie ->
                picasso.load(thismovie.posterUrl).into(moviePoster)
                picasso.load(thismovie.posterUrl).into(moviePosterSmall)
                movieTitle.text = thismovie.title
                viewModel.setStateEvent(
                    CreateMovieNightStateEvent.LoadMovie(
                        thismovie.imdbId
                    )
                )
            }
        }
        savedInstanceState?.let { savedState ->
            specifiedMovie = savedState.getParcelable(MOVIE_DETAIL)
            specifiedDate = savedState.getString(MOVIENIGHT_DATE)
            movieNightDatePicker.text = specifiedDate
            specifiedTime = savedState.getString(MOVIENIGHT_TIME)
            movieNightTimePicker.text = specifiedTime
            specifiedGuestUids = savedState.getParcelableArrayList<FollowUser>(GUEST_UIDS)!!
            selectedGuestsAdapter?.submitList(specifiedGuestUids)
        }
        setupRecyclerView()
        subscribeObservers()
        addListeners()
    }

    private fun setupRecyclerView() {
        guestsGallery.apply {
            adapter = selectedGuestsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun addListeners() {
        selectGuestsFab.setOnClickListener {
            startForResult.launch(Intent(activity, SelectGuestsActivity::class.java))
        }

        movieNightDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        movieNightTimePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                this,
                18,
                0,
                true
            )
            timePickerDialog.show()
        }

        saveMovieNightBtn.setOnClickListener {
            if (specifiedDate == null) {
                showSnackbar("Please specify a date.")
                return@setOnClickListener
            }
            if (specifiedMovie == null) {
                showSnackbar("Error!")
                return@setOnClickListener
            }
            if (specifiedTime == null) {
                showSnackbar("Please specify a time.")
            }

            viewModel.setStateEvent(
                CreateMovieNightStateEvent.SaveMovieNight(
                    MovieNight(
                        sharedPreferences.getString(Constants.CURRENT_USER_UID, "")!!,
                        specifiedGuestUids.map { it.uid },
                        specifiedMovie!!.imdbID!!,
                        specifiedDate!!,
                        specifiedTime!!
                    )
                )
            )
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            showProgressBar(dataState.loading)
            dataState.message?.let { consumableEvent ->
                consumableEvent.getContentIfNotHandled()?.let {
                    showSnackbar(it)
                }
            }
            dataState.data?.let { consumableEvent ->
                val state = consumableEvent.peekContent()
                when (state) {
                    is CreateMovieNightViewState.MovieLoaded -> {
                        setMovieData(state.details)
                        specifiedMovie = state.details
                        hideFragmentRootView(false)
                    }
                }
            }
        })
    }

    private fun setMovieData(details: MovieDetail) {

        val rating = getRating(details.imdbRating ?: "0")
        if (rating < 4.0) {
            tvImdbRating.setTextColor(Color.RED)
        } else if (rating > 7.9) {
            tvImdbRating.setTextColor(resources.getColor(R.color.colorHighlyRatedMovie))
        }
        tvImdbRating.text = rating.toString()
        parentalRating.text = "Rated: ${details.rated}"
        releaseYear.text = details.year
        description.text = details.plot

        details.genre
            ?.split(", ")
            ?.map { it.trim() }
            ?.forEach { genre ->
                genreChipGroup.addView(
                    Chip(activity).apply {
                        text = genre
                    }
                )
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(GUEST_UIDS, specifiedGuestUids)
        outState.putString(MOVIENIGHT_DATE, specifiedDate)
        outState.putString(MOVIENIGHT_TIME, specifiedTime)
        outState.putParcelable(MOVIE_DETAIL, specifiedMovie)
        super.onSaveInstanceState(outState)
    }

    private fun getRating(str: String): Float {
        try {
            val rating = str.toFloat()
            return rating
        } catch (e: Exception) {
            return 0.0f
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateString = "$year-${month + 1}-$dayOfMonth"
        specifiedDate = dateString
        movieNightDatePicker.text = dateString
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val paddedHour = hourOfDay.toString().padStart(2, '0')
        val paddedMinute = minute.toString().padStart(2, '0')
        specifiedTime = "$paddedHour:$paddedMinute"
        movieNightTimePicker.text = "$paddedHour:$paddedMinute"
    }

    override fun onDestroyView() {
        selectedGuestsAdapter = null
        super.onDestroyView()
    }
}