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
import android.net.Uri
import android.content.ActivityNotFoundException
import android.content.pm.ResolveInfo
import com.cebbinghaus.linkd.proto.Main

const val TAG_MAIN = "linkd.main"

class MainActivity: FlutterActivity() {
	var db: Database? = null;

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
		db = Database(this);
		var channel = Channel(flutterEngine, db!!);
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		db = Database(this);
		
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

		if (intent.action == Intent.ACTION_MAIN) {
			Log.i(TAG_MAIN, "Intent is ACTION_MAIN");
			return;
		}

		var value: String? = intent.data?.toString() ?: intent.getStringExtra("query");
		
		db!!.history.insert(IntentRecord(intent.action ?: "UNDEFINED", intent.flags, value));

		// We live with this for the sake of autocompletion
		if (value == null) {
			Log.i(TAG_MAIN, "Intent value is null");
			return;
		}

		Log.i(TAG_MAIN, "Intent value: " + value);
		intent.data = Uri.parse(value);

		if (value.lowercase().startsWith("fido:")) {
			Log.i(TAG_MAIN, "Intent is FIDO");

			var browserIntent = Intent(Intent.ACTION_VIEW, intent.data);
			launchIntent(browserIntent);
			return;
		}

		if(value.lowercase().startsWith("http")) {
			Log.i(TAG_MAIN, "Intent is HTTP");

			var packageName = "org.mozilla.firefox";

			intent.data?.let {
				if (it.host?.contains("test") == true) {
					packageName = "com.android.chrome";
				}
			}
			
			crateNewIntent(intent, packageName)?.let {
				launchIntent(it);
			}

		} else {
			Log.i(TAG_MAIN, "Intent is not HTTP");
		}
	}

	fun launchIntent(intent: Intent) {
		Log.i(TAG_MAIN, "launchIntent: " + intent);

		val pm = getPackageManager();

		// which packages could accept our intent
		// val resolveInfos: List<ResolveInfo> = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);

		// Log.i(TAG_MAIN, "ResolveInfos: " + resolveInfos);
		// for (resolveInfo in resolveInfos) {
		// 	Log.i(TAG_MAIN, "ResolveInfo: " + resolveInfo);
		// }

		val intentResolves = intent.resolveActivity(pm)?.let {
			Log.i(TAG_MAIN, "Activity resolved " + it);

			if (it.packageName == "com.cebbinghaus.linkd") {
				Log.w(TAG_MAIN, "Cyclical intent detected. Ignoring.");
				return;
			}
			intent.component = it;
			true
		} ?: false;

		if (!intentResolves) {
			Log.e(TAG_MAIN, "Unable to resolve the activity for the intent, Launching Chooser");
			// Create intent to show chooser
			val chooser = Intent.createChooser(intent, /* title */ null)

			safeStartActivityAndHide(chooser);
			return;
		}
		
		safeStartActivityAndHide(intent);
	}

	fun safeStartActivityAndHide(intent: Intent) {
		try {
			startActivity(intent);
			moveTaskToBack(true);
		} catch (e: ActivityNotFoundException) {
			Log.e(TAG_MAIN, "Unable to start the activity: " + e);
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