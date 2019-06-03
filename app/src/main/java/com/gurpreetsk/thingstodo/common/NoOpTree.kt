package com.gurpreetsk.thingstodo.common

import timber.log.Timber

class NoOpTree : Timber.Tree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    /* No-Op */
  }
}
