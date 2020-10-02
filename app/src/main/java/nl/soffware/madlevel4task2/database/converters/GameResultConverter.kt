package nl.soffware.madlevel4task2.database.converters

import androidx.room.TypeConverter
import nl.soffware.madlevel4task2.model.GameResult

class GameResultConverter{
    @TypeConverter
    fun fromGameResult(value: GameResult?): Int {
        return when(value){
            GameResult.PLAYER_WON -> 1
            GameResult.DRAW -> 0
            else -> -1
        }
    }
    @TypeConverter
    fun toGameResult(value: Int): GameResult {
        return when(value){
            1 -> GameResult.PLAYER_WON
            0 -> GameResult.DRAW
            else -> GameResult.PLAYER_LOST
        }
    }
}