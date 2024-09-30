package com.cebbinghaus.linkd

import android.util.Log
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.cebbinghaus.linkd.BackgroundReciever

const val TAG_BACKGROUND = "linkd.background"

public class BackgroundService : Service() {
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG_BACKGROUND, "Background service starting");
        return START_REDELIVER_INTENT;
    }

    override fun onCreate() {
        super.onCreate()
		Log.i(TAG_BACKGROUND, "Background service creating");
        
        // create IntentFilter
        val intentFilter = IntentFilter();

        // add actions
        intentFilter.addAction("android.intent.action.VIEW");
        intentFilter.addAction("android.intent.action.WEB_SEARCH");
        intentFilter.addAction("android.nfc.action.NDEF_DISCOVERED");
        intentFilter.addAction("android.intent.action.SEARCH");
        intentFilter.addAction("android.intent.action.SEND");

        Log.i(TAG_BACKGROUND, "Registering the Broadcast Receiver");
        // create and register receiver
        broadcastReceiver = BackgroundReciever();
        registerReceiver(broadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED);
    }

	override fun onBind(intent: Intent): IBinder? {
		return null
	}
}
