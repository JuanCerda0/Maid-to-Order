package pkg.maid_to_order.repository

import pkg.maid_to_order.data.local.OrderHistoryDao
import pkg.maid_to_order.data.local.OrderHistoryEntity

class OrderHistoryRepository(
    private val dao: OrderHistoryDao
) {
    val history = dao.observeHistory()

    suspend fun insert(entry: OrderHistoryEntity) {
        dao.insert(entry)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
