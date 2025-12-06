package bd.srabbijan.lenden.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import bd.srabbijan.lenden.core.PdfGenerator
import bd.srabbijan.lenden.core.PdfViewer
import bd.srabbijan.lenden.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<AppDatabase> {
        val dbFile = androidContext().getDatabasePath("lenden.db")
        Room.databaseBuilder<AppDatabase>(
            context = androidContext().applicationContext,
            name = dbFile.absolutePath
        )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
    }
    
    single { get<AppDatabase>().customerDao() }
    single { get<AppDatabase>().transactionDao() }
    single { PdfGenerator(androidContext()) }
    single { PdfViewer(androidContext()) }
}
