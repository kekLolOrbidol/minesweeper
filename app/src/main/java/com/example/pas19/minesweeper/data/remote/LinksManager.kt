package com.example.pas19.minesweeper.data.remote

import android.content.Context
import android.util.Log
import com.example.pas19.minesweeper.data.Preferences
import com.example.pas19.minesweeper.notif.CustomMessage
import com.facebook.applinks.AppLinkData


class LinksManager(val context: Context) {
    var url : String? = null
    var mainActivity : ApiTab? = null
    var exec = false
    val sPrefUrl = Preferences(context).apply { getSp("fb") }

    init{
        url = sPrefUrl.getStr("url")
        Log.e("Links", url.toString())
        if(url == null) tree()
    }

    fun attachWeb(api : ApiTab){
        mainActivity = api
    }

    private fun tree() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Log.e("DEEP", "SRABOTAL")
                    CustomMessage().scheduleMsg(context)
                    exec = true
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        sPrefUrl.putStr("url", url!!)
                        mainActivity?.execResponse(url!!)
                    }
                }
            }
        }
    }
}