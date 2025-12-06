package bd.srabbijan.lenden.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import bd.srabbijan.lenden.data.local.dao.CustomerDao
import bd.srabbijan.lenden.data.local.dao.TransactionDao
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.data.local.entity.TransactionEntity

@Database(entities = [CustomerEntity::class, TransactionEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun transactionDao(): TransactionDao
}

// The Room compiler generates the actual implementation
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
