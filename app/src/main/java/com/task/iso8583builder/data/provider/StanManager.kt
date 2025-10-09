package com.task.iso8583builder.data.provider

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
private val Context.dataStore by preferencesDataStore(name = "iso8583_prefs")

@Singleton
class StanManager @Inject constructor(
    @ApplicationContext private val context : Context
) {
    private val STAN_KEY = intPreferencesKey("last_stan")

    suspend fun getNextStan(): String {
        val currentStan = context.dataStore.data.map { preferences ->
            preferences[STAN_KEY] ?: 0
        }.first()
        val nextStan = if (currentStan >= 999999) 1 else currentStan +1
        context.dataStore.edit { preferences ->
            preferences[STAN_KEY] = nextStan
        }
        return String.format(Locale.ROOT,"%06d" , nextStan)
    }
}