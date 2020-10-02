package nl.soffware.madlevel4task2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.soffware.madlevel4task2.R
import nl.soffware.madlevel4task2.model.Game
import nl.soffware.madlevel4task2.repository.GameRepository

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistoryFragment : Fragment() {

    private lateinit var gameRepository: GameRepository
    private lateinit var historyAdapter: HistoryAdapter

    private val games = arrayListOf<Game>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyAdapter = HistoryAdapter(games, requireContext())
        initViews()
        gameRepository = GameRepository(requireContext())
        getGamesFromDatabase()
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapterI
        rv_history.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_history.adapter = historyAdapter
        rv_history.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(rv_history)
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteGame(gameToDelete)
                    }
                    getGamesFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun getGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.gateAllGames()
            }
            this@HistoryFragment.games.clear()
            this@HistoryFragment.games.addAll(games)
            historyAdapter.notifyDataSetChanged()
        }
    }
}