package bd.srabbijan.lenden.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY lastActivity DESC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%' ORDER BY lastActivity DESC")
    fun searchCustomers(query: String): Flow<List<CustomerEntity>>
    
    // Filter by balance type only
    @Query("SELECT * FROM customers WHERE balance > 0 ORDER BY lastActivity DESC")
    fun getCustomersWithPositiveBalance(): Flow<List<CustomerEntity>>
    
    @Query("SELECT * FROM customers WHERE balance < 0 ORDER BY lastActivity DESC")
    fun getCustomersWithNegativeBalance(): Flow<List<CustomerEntity>>
    
    // Combined search and filter queries
    @Query("SELECT * FROM customers WHERE (name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%') AND balance > 0 ORDER BY lastActivity DESC")
    fun searchCustomersWithPositiveBalance(query: String): Flow<List<CustomerEntity>>
    
    @Query("SELECT * FROM customers WHERE (name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%') AND balance < 0 ORDER BY lastActivity DESC")
    fun searchCustomersWithNegativeBalance(query: String): Flow<List<CustomerEntity>>
    
    // Total calculations (always from all customers)
    @Query("SELECT COALESCE(SUM(balance), 0.0) FROM customers WHERE balance > 0")
    fun getTotalGiven(): Flow<Double>
    
    @Query("SELECT COALESCE(SUM(balance), 0.0) FROM customers WHERE balance < 0")
    fun getTotalTaken(): Flow<Double>
    
    @Query("DELETE FROM customers WHERE id = :customerId")
    suspend fun deleteCustomer(customerId: Long)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY date DESC")
    fun getTransactionsForCustomer(customerId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE customerId = :customerId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsForCustomerByDateRange(customerId: Long, startDate: String, endDate: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
    
    @Query("DELETE FROM transactions WHERE customerId = :customerId")
    suspend fun deleteAllTransactionsForCustomer(customerId: Long)
    
    @Query("SELECT COUNT(*) FROM transactions WHERE customerId = :customerId")
    suspend fun getTransactionCount(customerId: Long): Int
}
