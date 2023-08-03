package com.croooownthreecoinsssss

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.croooownthreecoinsssss.PreferenceHelper.APPSFLYER_ID
import com.croooownthreecoinsssss.PreferenceHelper.NAMING_SUBS
import com.croooownthreecoinsssss.PreferenceHelper.PUSH_TRUE_MY
import com.croooownthreecoinsssss.PreferenceHelper.defaultPreference

import com.onesignal.OSNotificationAction
import com.onesignal.OneSignal
import java.util.*

class CrownThreeCoinsApp : Application() {
    var AF_DEV_KEY: String = ""
    var prefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        prefs = defaultPreference(applicationContext)

        val referrerClient: InstallReferrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        Log.d("TAG1", referrerClient.installReferrer.installReferrer.toString())

                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        Log.d("TAG1", referrerClient.installReferrer.installReferrer.toString())
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Log.d("TAG1", referrerClient.installReferrer.installReferrer.toString())
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("TAG", "Error")
            }
        })



        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {

                val status = Objects.requireNonNull(conversionData["af_status"]).toString()
                Log.d("TAG1", status)
                if (status == "Non-organic") {

                    if (conversionData["campaign"] != null) {
                        val arraySubs = conversionData["campaign"].toString().split("_").toTypedArray()
                        var counterSubs = 1
                        var namingSubs = "?"
                        for (subsParams in arraySubs) {
                            namingSubs += "kurilwik$counterSubs=$subsParams"
                            counterSubs++
                            namingSubs += "&"
                        }
                        prefs?.NAMING_SUBS = namingSubs
                    }
                }else{
                    Log.d("LOG_TAG", "af_status = organic")
                }

                for (attrName in conversionData.keys) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                Log.d("LOG_TAG", "error getting conversion data: $errorMessage")
            }

            override fun onAppOpenAttribution(conversionData: Map<String, String>) {
                for (attrName in conversionData.keys) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onAttributionFailure(errorMessage: String) {
                Log.d("LOG_TAG", "error onAttributionFailure : $errorMessage")
            }

        }
        val init = AppsFlyerLib.getInstance().init(
            AF_DEV_KEY,
            conversionListener,
            applicationContext
        )
        prefs?.APPSFLYER_ID = init.getAppsFlyerUID(applicationContext)
        AppsFlyerLib.getInstance().start(this)


        OneSignal.setAppId(applicationContext.getString(R.string.ONESIGNAL_APP_ID))
        OneSignal.initWithContext(applicationContext)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)

    }
}
