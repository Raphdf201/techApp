package net.raphdf201.techapp.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * Creates a datastore with the provided path, overriden by [net.raphdf201.techapp.createDataStoreIos]
 * and the private [androidx.datastore.preferences.preferencesDataStore] in [net.raphdf201.techapp.MainActivity]
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

/**
 * The dataStore file name, stored in the documents/datastore folder for ios and appDir/datastore for android
 */
internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"