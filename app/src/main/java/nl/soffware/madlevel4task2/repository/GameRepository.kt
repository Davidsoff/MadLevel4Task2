package nl.soffware.madlevel4task2.repository

import android.content.Context
import nl.soffware.madlevel4task2.dao.GameDao
import nl.soffware.madlevel4task2.database.GameRoomDatabase
import nl.soffware.madlevel4task2.model.Game
import nl.soffware.madlevel4task2.model.GameResult

class GameRepository(context: Context) {

    private var gameDao: GameDao

    init {
        val gameRoomDatabase = GameRoomDatabase.getDatabase(context)
        gameDao = gameRoomDatabase!!.gameDao()
    }

    suspend fun gateAllGames(): List<Game> {
        return gameDao.getAllGames()
    }

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun getGamesWithResult(result: GameResult): Int {
        return gameDao.getNumberOfGameResult(result)
    }

    suspend fun getLastGame(): Game? {
        return gameDao.getLastGame()
    }

}