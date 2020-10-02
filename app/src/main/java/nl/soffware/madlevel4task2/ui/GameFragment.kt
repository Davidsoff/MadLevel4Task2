package nl.soffware.madlevel4task2.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game_result.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.soffware.madlevel4task2.R
import nl.soffware.madlevel4task2.model.Game
import nl.soffware.madlevel4task2.model.GameResult
import nl.soffware.madlevel4task2.model.Move
import nl.soffware.madlevel4task2.repository.GameRepository
import nl.soffware.madlevel4task2.util.RandomEnum
import java.time.LocalDateTime
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameFragment : Fragment() {

    private lateinit var gameRepository: GameRepository

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepository = GameRepository(requireContext())

        renderLastGameResult()

        btn_rock.setOnClickListener {
            makeMove(Move.ROCK)
        }

        btn_paper.setOnClickListener {
            makeMove(Move.PAPER)
        }

        btn_scissors.setOnClickListener {
            makeMove(Move.SCISSORS)
        }
    }

    private fun makeMove(playerMove: Move) {
        val r: RandomEnum<Move> = RandomEnum(Move::class.java)
        val computerMove = r.random()
        val date = Calendar.getInstance().time
        val game = Game.create(playerMove, computerMove, date)
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            renderLastGameResult()
        }
    }


    private fun renderLastGameResult() {
        mainScope.launch {
            val game = withContext(Dispatchers.IO) {
                gameRepository.getLastGame()
            }
            if (game == null) {
                single_game_result.visibility = View.INVISIBLE
            } else {
                val computerMove = game.getComputerMove()
                    .toString()
                    .toLowerCase(Locale.getDefault())
                    .capitalize(Locale.getDefault())

                val playerMove = game.playerMove
                    .toString()
                    .toLowerCase(Locale.getDefault())
                    .capitalize(Locale.getDefault())

                single_game_result.visibility = View.VISIBLE
                single_game_result.ivComputerMove.setImageDrawable(getMoveImage(game.getComputerMove()))
                single_game_result.ivPlayerMove.setImageDrawable(getMoveImage(game.playerMove))
                single_game_result.tvResult.text = getResultString(game.result)
                single_game_result.tvPlayer.text =
                    resources.getString(R.string.player_move_description, playerMove)
                single_game_result.tvComputer.text =
                    resources.getString(R.string.computer_move_description, computerMove)
                single_game_result.tvDate.visibility = View.INVISIBLE
            }
            updateGameStats()
        }
    }

    private fun updateGameStats() {
        mainScope.launch {
            val won = withContext(Dispatchers.IO){
                gameRepository.getGamesWithResult(GameResult.PLAYER_WON)
            }
            val draw = withContext(Dispatchers.IO){
                gameRepository.getGamesWithResult(GameResult.DRAW)
            }
            val lost = withContext(Dispatchers.IO){
                gameRepository.getGamesWithResult(GameResult.PLAYER_LOST)
            }
            tvStats.text = resources.getString(R.string.stats, won, draw, lost)
        }
    }

    private fun getResultString(result: GameResult): String{
        val id = when(result){
            GameResult.PLAYER_WON -> R.string.player_won
            GameResult.DRAW -> R.string.draw
            else -> R.string.computer_won
        }
        return resources.getString(id, null)
    }

    private fun getMoveImage(move: Move): Drawable? {
        val id = when(move) {
            Move.ROCK -> R.drawable.rock
            Move.PAPER -> R.drawable.paper
            else -> R.drawable.scissors
        }
        return resources.getDrawable(id, null)
    }
}