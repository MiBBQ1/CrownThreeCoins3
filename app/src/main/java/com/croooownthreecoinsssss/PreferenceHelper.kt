package com.croooownthreecoinsssss

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    val LINK = "LINK"
    val MY_FIRST_TIME = "my_first_time"
    val PREFS = "runWeb"
    val PREFS2 = "runGame"
    val NAMING = "naming"
    val PUSH_TRUE = "pushtrue"
    val APPS_FLYER_UID = "AppsFlyerUID"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }
    var SharedPreferences.URL_LINK
        get() = getString(LINK, "")
        set(value) {
            editMe {
                it.putString(LINK, value)
            }
        }

    var SharedPreferences.FIRST_OPEN
        get() = getBoolean(MY_FIRST_TIME, false)
        set(value) {
            editMe {
                it.putBoolean(MY_FIRST_TIME, value)
            }
        }

    var SharedPreferences.PUSH_TRUE_MY
        get() = getBoolean(PUSH_TRUE, false)
        set(value) {
            editMe {
                it.putBoolean(PUSH_TRUE, value)
            }
        }



    var SharedPreferences.GAME_START
        get() = getBoolean(PREFS2, false)
        set(value) {
            editMe {
                it.putBoolean(PREFS2, value)
            }
        }

    var SharedPreferences.STATE_ADS
        get() = getBoolean(PREFS, false)
        set(value) {
            editMe {
                it.putBoolean(PREFS, value)
            }
        }

    var SharedPreferences.APPSFLYER_ID
        get() = getString(APPS_FLYER_UID, "")
        set(value) {
            editMe {
                it.putString(APPS_FLYER_UID, value)
            }
        }



    var SharedPreferences.NAMING_SUBS
        get() = getString(NAMING, "")
        set(value) {
            editMe {
                it.putString(NAMING, value)
            }
        }

}