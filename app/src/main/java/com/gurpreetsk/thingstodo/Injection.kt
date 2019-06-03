package com.gurpreetsk.thingstodo

import android.content.Context

object Injection {
  fun getDatabase(context: Context): Database =
      (context.applicationContext as ThingsToDoApplication).getDatabase()
}
