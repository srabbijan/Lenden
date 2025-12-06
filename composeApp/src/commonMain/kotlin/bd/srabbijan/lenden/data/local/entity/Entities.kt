package bd.srabbijan.lenden.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val balance: Double, // Positive = Taken (You get), Negative = Given (You owe) - or vice versa, let's define: Positive = You will get, Negative = You have to give.
    val lastActivity: String
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val amount: Double,
    val type: String, // "GIVEN" or "TAKEN"
    val note: String,
    val date: String
)
