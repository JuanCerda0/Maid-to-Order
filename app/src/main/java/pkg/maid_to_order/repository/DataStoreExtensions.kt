package pkg.maid_to_order.repository

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Simple property delegate to get DataStore<Preferences> from Context
val Context.dataStore by preferencesDataStore(name = "maid_prefs")
