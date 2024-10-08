package com.cebbinghaus.linkd

import android.util.Log
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import com.cebbinghaus.linkd.proto.Main

const val CHANNEL = "com.cebbinghaus.linkd";

const val TAG_CHANNEL = "linkd.channel";

val REQUEST_HISTORY_REQUEST = Main.Requests.forNumber(Main.Requests.REQUEST_HISTORY_REQUEST_VALUE).valueDescriptor.toString();
val REQUEST_HISTORY_RESPONSE = Main.Requests.forNumber(Main.Requests.REQUEST_HISTORY_RESPONSE_VALUE).valueDescriptor.toString();


class Channel(engine: FlutterEngine, database: Database) : MethodCallHandler {
	private val channel = MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL);
	private val database = database;


	init {
		channel.setMethodCallHandler(this);
	}

	fun sendMessage(message: String) {
		try {
			channel.invokeMethod("sendMessage", message)
		} catch (e: Exception) {
			println("Failed to send message: '${e.message}'.")
		}
	}

	fun sendHistory(history: Main.HistoryResponse) {
		try {
			channel.invokeMethod(REQUEST_HISTORY_RESPONSE, history.toByteArray())
		} catch (e: Exception) {
			println("Failed to send history: '${e.message}'.")
		}
	}

 	override fun onMethodCall(method: MethodCall, result: Result) {
		when (method.method) {
			REQUEST_HISTORY_REQUEST -> {
				Log.i(TAG_CHANNEL, "getHistory");

				var historyResponse = Main.HistoryResponse.newBuilder();
				database.history.getAll().forEach {
					historyResponse.addHistory(it.toHistory());
				}

				result.success(historyResponse.build().toByteArray());
			}
			else -> {
				result.notImplemented();
			}
		}
	}
}