package org.itmo.mop.animalmap.data.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenPersistence @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {

    private val prefs: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
    }

    fun saveTokenImmediately(token: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .commit()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun isTokenExists(): Boolean = getToken() != null

    fun clearImmediately() {
        prefs.edit()
            .clear()
            .commit()
    }

    private companion object {
        const val SHARED_PREFERENCES_TOKEN = "KEY_TOKEN_SHARED_PREFERENCES"
        const val KEY_TOKEN = "KEY_TOKEN_SHARED_PREFERENCES"
    }
}