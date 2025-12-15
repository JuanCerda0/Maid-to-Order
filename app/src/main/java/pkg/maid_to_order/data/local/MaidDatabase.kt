package pkg.maid_to_order.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [OrderHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MaidDatabase : RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MaidDatabase? = null

        fun getInstance(context: Context): MaidDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MaidDatabase::class.java,
                    "maid_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
