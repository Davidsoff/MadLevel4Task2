package nl.soffware.madlevel4task2.database.converters

import androidx.room.TypeConverter
import nl.soffware.madlevel4task2.model.Move

class MoveConverter {
    @TypeConverter
    fun fromMove(value: Move?): Int {
        return when(value){
            Move.ROCK -> 0
            Move.PAPER -> 1
            else -> 2
        }
    }
    @TypeConverter
    fun toMove(value: Int): Move {
        return when(value){
            0 -> Move.ROCK
            1 -> Move.PAPER
            else -> Move.SCISSORS
        }
    }
}