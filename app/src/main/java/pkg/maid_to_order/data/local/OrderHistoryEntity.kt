package pkg.maid_to_order.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_history")
data class OrderHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tableNumber: String?,
    val total: Double,
    val itemCount: Int,
    val notes: String?,
    val createdAt: Long,
    val itemsSummary: String
)
