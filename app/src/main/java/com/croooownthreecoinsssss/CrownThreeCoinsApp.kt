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
    companion object {
        var AF_DEV_KEY: String = ""

        var prefs: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = defaultPreference(applicationContext)
        AF_DEV_KEY = applicationContext.getString(R.string.AF_DEV_KEY)
        OneSignal.setAppId(applicationContext.getString(R.string.ONESIGNAL_APP_ID))
        OneSignal.initWithContext(applicationContext)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setNotificationOpenedHandler { result ->
            val actionId = result.action.actionId
            val type: OSNotificationAction.ActionType? = result.action.type // "ActionTaken" | "Opened"
            val title = result.notification.title
            if(prefs?.PUSH_TRUE_MY != true) {
                prefs?.PUSH_TRUE_MY = true
            }
        }

        var referrerClient: InstallReferrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })



        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {

                val status = Objects.requireNonNull(conversionData["af_status"]).toString()

                if (status == "Non-organic") {
                    if (conversionData["adgroup_id"] != null) {

                        Log.d("LOG_TAG", "ad_id: " + conversionData["adgroup_id"].toString())

                    }

                    if (conversionData["adset"] != null) {

                        Log.d("LOG_TAG", "adset: " + conversionData["adset"].toString())
                    }

                    if (conversionData["adset_id"] != null) {

                        Log.d("LOG_TAG", "adset_id: " + conversionData["adset_id"].toString())
                    }
                    if (conversionData["campaign_id"] != null) {

                        Log.d("LOG_TAG", "campaign_id: " + conversionData["campaign_id"].toString())
                    }
                    if (conversionData["campaign"] != null) {
                        var array = conversionData["campaign"].toString().split("_").toTypedArray()
                        var counter = 1
                        var naming = "?"
                        for (i in array) {
                            Log.d("LOG_TAG", "sub_id_$counter=$i")
                            naming += "sub_id_$counter=$i"
                            counter++
                            naming += "&"
                        }
                        prefs?.NAMING_SUBS = naming
                    }
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
    }

}
