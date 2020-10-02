package nl.soffware.madlevel4task2.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_game_result.view.*
import nl.soffware.madlevel4task2.R
import nl.soffware.madlevel4task2.model.Game
import nl.soffware.madlevel4task2.model.GameResult
import nl.soffware.madlevel4task2.model.Move
import java.util.*

class HistoryAdapter(private val games: List<Game>, private val context: Context) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val resources: Resources = itemView.resources

        fun databind(game: Game){
            val computerMove = game.getComputerMove()
                .toString()
                .toLowerCase(Locale.getDefault())
                .capitalize(Locale.getDefault())

            val playerMove = game.playerMove
                .toString()
                .toLowerCase(Locale.getDefault())
                .capitalize(Locale.getDefault())

            val formatter = SimpleDateFormat(resources.getString(R.string.result_date_format), Locale.US)

            itemView.ivComputerMove.setImageDrawable(getMoveImage(game.getComputerMove()))
            itemView.ivPlayerMove.setImageDrawable(getMoveImage(game.playerMove))
            itemView.tvResult.text = getResultString(game.result)
            itemView.tvPlayer.text = resources.getString(R.string.player_move_description, playerMove)
            itemView.tvComputer.text = resources.getString(R.string.computer_move_description, computerMove)
            itemView.tvDate.text = formatter.format(game.date)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_game_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }
}