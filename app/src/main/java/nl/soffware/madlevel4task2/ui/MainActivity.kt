package nl.soffware.madlevel4task2.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.soffware.madlevel4task2.R
import nl.soffware.madlevel4task2.repository.GameRepository

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var gameRepository: GameRepository

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        navController = findNavController(R.id.nav_host_fragment)
        gameRepository = GameRepository(this.baseContext)

        backButtonToggler()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            menu!!
            if (destination.id in arrayOf(R.id.HistoryFragment)) {
                menu.findItem(R.id.action_history).isVisible = false
                menu.findItem(R.id.action_delete_all).isVisible = true
            } else {
                menu.findItem(R.id.action_history).isVisible = true
                menu.findItem(R.id.action_delete_all).isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                navController.navigate(R.id.action_GameFragment_to_HistoryFragment)
                true
            }
            R.id.action_delete_all -> {
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteAllGames()
                    }
                    navController.navigate(R.id.action_HistoryFragment_to_GameFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun backButtonToggler(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.HistoryFragment)) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true);
                supportActionBar!!.setDisplayShowHomeEnabled(true);
            } else {
                supportActionBar!!.setDisplayHomeAsUpEnabled(false);
                supportActionBar!!.setDisplayShowHomeEnabled(false);
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}