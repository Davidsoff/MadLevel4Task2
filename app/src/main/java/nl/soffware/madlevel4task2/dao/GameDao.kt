package nl.soffware.madlevel4task2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import nl.soffware.madlevel4task2.model.Game
import nl.soffware.madlevel4task2.model.GameResult

@Dao
interface GameDao {
    @Query("SELECT * FROM game_table ORDER BY date DESC")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(*) FROM game_table WHERE result IS :result")
    suspend fun getNumberOfGameResult(result: GameResult): Int

    @Query("SELECT * from game_table ORDER BY date DESC LIMIT 1")
    suspend fun getLastGame(): Game?
}
