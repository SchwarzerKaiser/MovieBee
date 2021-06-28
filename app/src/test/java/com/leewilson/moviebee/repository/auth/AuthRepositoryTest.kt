package com.leewilson.moviebee.repository.auth

import android.content.SharedPreferences
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leewilson.moviebee.persistence.UserPropertiesDao
import com.leewilson.moviebee.util.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AuthRepositoryTest {

    private val email = "lee.a.wilson90@gmail.com"
    private val password = "fakepassword"
    private val CORRECT_USER_ID = "CORRECT_USER_ID"

    private lateinit var SUT: AuthRepository

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var authResultTask: Task<AuthResult>

    @Mock
    private lateinit var mockResult: AuthResult

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    @Mock
    private lateinit var mockUserPropertiesDao: UserPropertiesDao

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        // Creating a Task to mock out result
        val completionSource = TaskCompletionSource<AuthResult>()
        completionSource.setResult(mockResult)
        authResultTask = completionSource.task

        `when`(firebaseAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(authResultTask)

        `when`(mockResult.user)
            .thenReturn(mockFirebaseUser)

        `when`(mockFirebaseUser.uid)
            .thenReturn("CORRECT_USER_ID")

        `when`(mockUserPropertiesDao.searchByEmail(email))
            .thenReturn(null)

        `when`(mockSharedPreferences.getString(Constants.PREVIOUS_AUTH_USER, null))
            .thenReturn(null)

        SUT = AuthRepository(
            firebaseAuth,
            mockUserPropertiesDao,
            mockSharedPreferences,
            mockSharedPreferencesEditor
        )
    }

    @Test
    fun test_correctCredentialsEntered_successOutputState() = runBlockingTest {
        val resultState = SUT.loginWithCredentials(email, password)
        val result = resultState.data!!.getContentIfNotHandled()!!.uid
        assertEquals(CORRECT_USER_ID, result)
    }

    @Test
    fun test_emptyStringsEntered_errorOutputState() = runBlockingTest {
        val resultState = SUT.loginWithCredentials("", "")
        val result = resultState.message!!.getContentIfNotHandled()
        assertEquals(Constants.MISSING_FIELDS, result)
    }
}

