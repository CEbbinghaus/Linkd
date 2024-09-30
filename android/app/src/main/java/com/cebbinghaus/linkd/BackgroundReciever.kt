package com.cebbinghaus.linkd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

const val TAG_RECIEVER = "linkd.reciever";

class BackgroundReciever : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		Log.i(TAG_RECIEVER, "Broadcast received");

		// Log.i(TAG_BACKGROUND, "Broadcast received" + intent);
	}
}