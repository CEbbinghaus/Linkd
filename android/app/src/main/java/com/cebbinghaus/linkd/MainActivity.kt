package com.cebbinghaus.linkd

import io.flutter.embedding.android.FlutterActivity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.Context
import android.util.Log
import android.os.Bundle 
import android.app.ActivityManager
import kotlin.text.startsWith
import android.os.Handler
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.embedding.engine.FlutterEngine
import com.cebbinghaus.linkd.BackgroundService
import android.os.Looper
import android.content.ActivityNotFoundException
import android.content.pm.ResolveInfo
import com.cebbinghaus.linkd.proto.Main

const val TAG_MAIN = "linkd.main"

class MainActivity: FlutterActivity() {
	private fun isServiceRunning(serviceClass: Class<*>): Boolean {
		val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
		for (process in activityManager.runningAppProcesses) {
			if (process.processName == serviceClass.name) {
				return true
			}
		}
		return false
	}

	override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
		super.configureFlutterEngine(flutterEngine);
		var channel = Channel(flutterEngine);
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.i(TAG_MAIN, "onCreate");
		Log.i(TAG_MAIN, "Descriptor: " + Main.Requests.forNumber(Main.Requests.REQUEST_HISTORY_RESPONSE_VALUE).valueDescriptor.toString());

		intent?.let {
			handleIntent(it)
		}
		
		// Log.i(TAG_MAIN, "Starting the background service");
		// Log.i("linkd.background", "Test Log");
		// startService(Intent(this, BackgroundService::class.java));

		// Handler(Looper.getMainLooper()).postDelayed({
		// 	// Check if the BackgroundService is running
		// 	val isRunning = isServiceRunning(BackgroundService::class.java)
		// 	Log.i(TAG_MAIN, "Is BackgroundService running: $isRunning")
		// }, 2000)
	}

	override fun onNewIntent(intent: Intent) {
		Log.i(TAG_MAIN, "onNewIntent");
		handleIntent(intent);
	}

	fun handleIntent(intent: Intent) {
		Log.i(TAG_MAIN, "handleIntent: " + intent);
		
		if(intent.data?.scheme?.startsWith("http") == true) {
			Log.i(TAG_MAIN, "Intent is HTTP");

			var packageName = "org.mozilla.firefox";

			intent.data?.let {
				if (it.host?.contains("test") == true) {
					packageName = "com.android.chrome";
				}
			}
			
			var intent = crateNewIntent(intent, packageName);
			
			if (intent == null) {
				Log.e(TAG_MAIN, "Unable to create new intent");
				return;
			}

			
			Log.i(TAG_MAIN, "new intent: " + intent);

			val pm = getPackageManager();

			val resolveInfos: List<ResolveInfo> = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);

			Log.i(TAG_MAIN, "ResolveInfos: " + resolveInfos);
			for (resolveInfo in resolveInfos) {
				Log.i(TAG_MAIN, "ResolveInfo: " + resolveInfo);
			}

			if (intent.resolveActivity(pm)?.let {
				Log.i(TAG_MAIN, "Activity resolved " + it);

				if (it.packageName == "com.cebbinghaus.linkd") {
					Log.w(TAG_MAIN, "Cyclical intent detected. Ignoring.");
					return;
				}
				intent.component = it;
				true
			} != true) {
				Log.e(TAG_MAIN, "Unable to resolve the activity for the intent.");
				// Create intent to show chooser
				val chooser = Intent.createChooser(intent, /* title */ null)

				// Try to invoke the intent.
				try {
					startActivity(chooser)
				} catch (e: ActivityNotFoundException) {
					// Define what your app should do if no activity can handle the intent.
				}
				return;
			}

			// intent.`package` = "com.mozilla.firefox";
			// activity.startActivity(intent);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (e: ActivityNotFoundException) {
				Log.e(TAG_MAIN, "Unable to start the activity: " + e);
			}
			finish();
		}
	}

	fun crateNewIntent(intent: Intent, packageName: String): Intent? {
		//var new_intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		
		// if(new_intent == null) {
		// 	Log.e(TAG_MAIN, "Unable to create a new intent for the package: " + packageName);
		// 	return null;
		// }

		var new_intent = Intent(intent.action);
		new_intent.`package` = packageName;
		// new_intent.component = ComponentName("org.mozilla.firefox", "org.mozilla.fenix.IntentReceiverActivity");
		new_intent.setDataAndType(intent.data, intent.type);
		return new_intent;
	}

}

/*
Intent buildIntent(
      @Nullable String action,
      @Nullable Integer flags,
      @Nullable String category,
      @Nullable Uri data,
      @Nullable Bundle arguments,
      @Nullable String packageName,
      @Nullable ComponentName componentName,
      @Nullable String type) {
    if (applicationContext == null) {
      Log.wtf(TAG, "Trying to build an intent before the applicationContext was initialized.");
      return null;
    }

    Intent intent = new Intent();

    if (action != null) {
      intent.setAction(action);
    }
    if (flags != null) {
      intent.addFlags(flags);
    }
    if (!TextUtils.isEmpty(category)) {
      intent.addCategory(category);
    }
    if (data != null && type == null) {
      intent.setData(data);
    }
    if (type != null && data == null) {
      intent.setType(type);
    }
    if (type != null && data != null) {
      intent.setDataAndType(data, type);
    }
    if (arguments != null) {
      intent.putExtras(arguments);
    }
    if (!TextUtils.isEmpty(packageName)) {
      intent.setPackage(packageName);
      if (componentName != null) {
        intent.setComponent(componentName);
      }
    }

    return intent;
  } */