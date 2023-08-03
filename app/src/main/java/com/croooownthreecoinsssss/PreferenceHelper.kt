package com.croooownthreecoinsssss

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {



        val LINK = "urllink"
        val MY_FIRST_TIME = "open_first"
        val PREFS = "vViewOp"
        val PREFS2 = "bGameOp"
        val NAMING = "nameSubs"
        val APPS_FLYER_UID = "apsoUID"

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