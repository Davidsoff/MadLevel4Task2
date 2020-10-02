package nl.soffware.madlevel4task2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.soffware.madlevel4task2.dao.GameDao
import nl.soffware.madlevel4task2.database.converters.DateConverter
import nl.soffware.madlevel4task2.database.converters.GameResultConverter
import nl.soffware.madlevel4task2.database.converters.MoveConverter
import nl.soffware.madlevel4task2.model.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, GameResultConverter::class, MoveConverter::class)
abstract class GameRoomDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var gameRoomDatabaseInstance: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase? {
            if (gameRoomDatabaseInstance == null) {
                synchronized(GameRoomDatabase::class.java) {
                    if (gameRoomDatabaseInstance == null) {
                        gameRoomDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,
                                GameRoomDatabase::class.java, DATABASE_NAME
                            ).build()
                    }
                }
            }
            return gameRoomDatabaseInstance
        }
    }
}