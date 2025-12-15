package pkg.maid_to_order.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderHistoryDao {
    @Query("SELECT * FROM order_history ORDER BY createdAt DESC")
    fun observeHistory(): Flow<List<OrderHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: OrderHistoryEntity)

    @Query("DELETE FROM order_history")
    suspend fun clearAll()
}
