package com.gurpreetsk.thingstodo

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gurpreetsk.thingstodo.audios.AudiosFragment
import com.gurpreetsk.thingstodo.audios.TAG_AUDIOS_FRAGMENT
import com.gurpreetsk.thingstodo.books.BooksFragment
import com.gurpreetsk.thingstodo.books.TAG_BOOKS_FRAGMENT
import com.gurpreetsk.thingstodo.likings.LikingsFragment
import com.gurpreetsk.thingstodo.likings.TAG_LIKINGS_FRAGMENT
import com.gurpreetsk.thingstodo.misc.MiscFragment
import com.gurpreetsk.thingstodo.misc.TAG_MISC_FRAGMENT
import com.gurpreetsk.thingstodo.movies.MoviesFragment
import com.gurpreetsk.thingstodo.movies.TAG_MOVIES_FRAGMENT
import kotlinx.android.synthetic.main.main_activity.bottomNavigationView

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)

    setupBottomNavigationView()
  }

  private fun setupBottomNavigationView() {
    bottomNavigationView.selectedItemId = R.id.nav_icon_movies
    showFragment(ItemType.MOVIES)

    bottomNavigationView.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.nav_icon_movies  -> showFragment(ItemType.MOVIES)
        R.id.nav_icon_audios  -> showFragment(ItemType.AUDIOS)
        R.id.nav_icon_books   -> showFragment(ItemType.BOOKS)
        R.id.nav_icon_likings -> showFragment(ItemType.LIKINGS)
        R.id.nav_icon_misc    -> showFragment(ItemType.MISC)
      }

      return@setOnNavigationItemSelectedListener true
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_navigation_menu, menu)
    return true
  }

  private fun showFragment(itemType: ItemType) {
    val (fragment, fragmentTag) = getFragmentInformation(itemType)

    supportFragmentManager
        .beginTransaction()
        .replace(R.id.mainContainer, fragment, fragmentTag)
        .commit()
  }

  private fun getFragmentInformation(itemType: ItemType): Pair<Fragment, String> {
    return when (itemType) {
      ItemType.MOVIES   -> MoviesFragment.newInstance() to TAG_MOVIES_FRAGMENT
      ItemType.AUDIOS   -> AudiosFragment.newInstance() to TAG_AUDIOS_FRAGMENT
      ItemType.BOOKS    -> BooksFragment.newInstance() to TAG_BOOKS_FRAGMENT
      ItemType.LIKINGS  -> LikingsFragment.newInstance() to TAG_LIKINGS_FRAGMENT
      ItemType.MISC     -> MiscFragment.newInstance() to TAG_MISC_FRAGMENT
    }
  }
}

private enum class ItemType {
  MOVIES,
  AUDIOS,
  BOOKS,
  LIKINGS,
  MISC,
}
