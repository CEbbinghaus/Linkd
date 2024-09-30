import 'package:flutter/services.dart';
import 'package:linkd/proto/main.pb.dart';

const CHANNEL_NAME = "com.cebbinghaus.linkd";

class Channel {
  static const MethodChannel channel = const MethodChannel(CHANNEL_NAME);

  static Future<String> get platformVersion async {
    final String version = await channel.invokeMethod(Requests.REQUEST_HISTORY_REQUEST.toString());
    return version;
  }

  static Future<History> get history async {
    final Uint8List history = await channel.invokeMethod(Requests.REQUEST_HISTORY_REQUEST.toString());
    return History.fromBuffer(history);
  }
}