package bd.srabbijan.lenden.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import bd.srabbijan.lenden.core.PdfGenerator
import bd.srabbijan.lenden.core.PdfViewer
import bd.srabbijan.lenden.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformModule = module {
    single<AppDatabase> {
        val dbFile = NSHomeDirectory() + "/lenden.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFile
        )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
    }
    
    single { get<AppDatabase>().customerDao() }
    single { get<AppDatabase>().transactionDao() }
    single { PdfGenerator() }
    single { PdfViewer() }
}
