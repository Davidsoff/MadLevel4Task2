package nl.soffware.madlevel4task2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.Exception

@Entity(tableName = "game_table")
data class Game(
    @ColumnInfo(name = "player_move")
    var playerMove: Move,
    @ColumnInfo(name = "result")
    var result: GameResult,
    @ColumnInfo(name = "date")
    var date: Date,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) {

    companion object Factory {
        fun create(playerMove: Move, computerMove: Move, date: Date): Game{
            val result = when{
                playerMove == Move.ROCK     && computerMove == Move.SCISSORS -> GameResult.PLAYER_WON
                playerMove == Move.ROCK     && computerMove == Move.ROCK     -> GameResult.DRAW
                playerMove == Move.ROCK     && computerMove == Move.PAPER    -> GameResult.PLAYER_LOST
                playerMove == Move.PAPER    && computerMove == Move.ROCK     -> GameResult.PLAYER_WON
                playerMove == Move.PAPER    && computerMove == Move.PAPER    -> GameResult.DRAW
                playerMove == Move.PAPER    && computerMove == Move.SCISSORS -> GameResult.PLAYER_LOST
                playerMove == Move.SCISSORS && computerMove == Move.PAPER    -> GameResult.PLAYER_WON
                playerMove == Move.SCISSORS && computerMove == Move.SCISSORS -> GameResult.DRAW
                playerMove == Move.SCISSORS && computerMove == Move.ROCK     -> GameResult.PLAYER_LOST
                else -> throw Exception("Impossible state combination, how on earth did you manage this?")
            }
            return Game(playerMove, result, date)
        }
    }

    fun getComputerMove(): Move{
        return when{
            playerMove == Move.ROCK     && result == GameResult.PLAYER_WON  -> Move.SCISSORS
            playerMove == Move.ROCK     && result == GameResult.DRAW        -> Move.ROCK
            playerMove == Move.ROCK     && result == GameResult.PLAYER_LOST -> Move.PAPER
            playerMove == Move.PAPER    && result == GameResult.PLAYER_WON  -> Move.ROCK
            playerMove == Move.PAPER    && result == GameResult.DRAW        -> Move.PAPER
            playerMove == Move.PAPER    && result == GameResult.PLAYER_LOST -> Move.SCISSORS
            playerMove == Move.SCISSORS && result == GameResult.PLAYER_WON  -> Move.PAPER
            playerMove == Move.SCISSORS && result == GameResult.DRAW        -> Move.SCISSORS
            playerMove == Move.SCISSORS && result == GameResult.PLAYER_LOST -> Move.ROCK
            else -> throw Exception("Impossible state combination, how on earth did you manage this?")
        }
    }
}