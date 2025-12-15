package pkg.maid_to_order.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pkg.maid_to_order.data.local.MaidDatabase
import pkg.maid_to_order.data.local.OrderHistoryEntity
import pkg.maid_to_order.repository.OrderHistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OrderHistoryItem(
    val id: Long,
    val tableNumber: String?,
    val total: Double,
    val itemCount: Int,
    val notes: String?,
    val formattedDate: String,
    val itemsSummary: String
)

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderHistoryRepository(
        MaidDatabase.getInstance(application).orderHistoryDao()
    )

    val history: StateFlow<List<OrderHistoryItem>> = repository.history
        .map { list -> list.map { it.toItem() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun OrderHistoryEntity.toItem(): OrderHistoryItem {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return OrderHistoryItem(
            id = id,
            tableNumber = tableNumber,
            total = total,
            itemCount = itemCount,
            notes = notes,
            formattedDate = formatter.format(Date(createdAt)),
            itemsSummary = itemsSummary
        )
    }
}
