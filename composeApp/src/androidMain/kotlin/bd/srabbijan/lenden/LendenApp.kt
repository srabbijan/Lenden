package bd.srabbijan.lenden

import android.app.Application
import bd.srabbijan.lenden.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class LendenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@LendenApp)
        }
    }
}
