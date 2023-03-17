package nl.eduid.wallet.android.app

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.zone.ZoneRulesProvider

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupMavericks()
        setupThreeTenBp()
    }

    private fun setupMavericks() {
        Mavericks.initialize(this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupThreeTenBp() {

        // Init ThreeTenABP
        AndroidThreeTen.init(this)

        // Query the ZoneRulesProvider so that it is loaded on a background coroutine
        GlobalScope.launch(Dispatchers.IO) {
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }

}
