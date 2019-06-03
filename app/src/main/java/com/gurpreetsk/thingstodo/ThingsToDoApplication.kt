package com.gurpreetsk.thingstodo

import android.app.Application
import com.gurpreetsk.thingstodo.common.NoOpTree
import com.squareup.sqldelight.android.AndroidSqliteDriver
import timber.log.Timber

class ThingsToDoApplication : Application() {
  private val sqliteDriver by lazy { AndroidSqliteDriver(Database.Schema, this, "thingstodo.db") }
  private val appDatabase by lazy { Database(sqliteDriver) }

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(NoOpTree())
    }
  }

  fun getDatabase(): Database = appDatabase
}
