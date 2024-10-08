import 'dart:async';

import 'package:flutter/material.dart';
import 'package:linkd/channel.dart';
// import 'package:receive_intent/receive_intent.dart' as IntentLib;
// import 'package:url_launcher/url_launcher.dart';
// Future<void> ProcessIntent(IntentLib.Intent? intent) async {
//   if (intent == null || intent.isNull) {
//     print("Received intent is null or not valid");
//     return;
//   }

//   String? query;

//   switch (intent.action) {
//     case "android.intent.action.VIEW":
//       print("Received VIEW intent");
//       query = intent.data;
//       break;
//     case "android.intent.action.SEND":
//       print("Received SEND intent");
//       break;
//     case "android.intent.action.WEB_SEARCH":
//       print("Received SEND_MULTIPLE intent");
//       query = intent.extra?["query"] as String?;
//       break;
//     case "android.intent.action.SEARCH":
//       print("Received SEND_MULTIPLE intent");
//       break;
//     default:
//       print("Received unknown intent: ${intent.action}");
//       break;
//   }

//   if (query == null) {
//     print("Unable to determine query");
//     return;
//   }

//   print("Received query: $query");

//   if (query.startsWith("http")) {
//     final nextIntent = AndroidIntent(
//       action: intent.action,
//       package: 'org.mozilla.firefox',
//       componentName: "org.mozilla.fenix.IntentReceiverActivity",
//       data: query,
//     );
//     await nextIntent.launch();
//   } else if (query.toLowerCase().startsWith("fido:")) {
//     final Uri url = Uri.parse(query);
//     if (!await launchUrl(url)) {
//       throw Exception('Could not launch $url');
//     }
//   }
// }

Future<void> main() async {
  // WidgetsFlutterBinding.ensureInitialized();

  // print("Starting app");
  // final receivedIntent = await IntentLib.ReceiveIntent.getInitialIntent();
  // ProcessIntent(receivedIntent);

  // if (receivedIntent != null && receivedIntent.isNotNull) {
  //   print("Started with intent: $receivedIntent");
  // }

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Linkd',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a purple toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Link History'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  StreamSubscription? _sub;

  List<String> recieved = [];

  @override
  void initState() {
    super.initState();
    
    load();
    // _sub = IntentLib.ReceiveIntent.receivedIntentStream.listen((intent) {
    //   WidgetsFlutterBinding.ensureInitialized();
    //   ProcessIntent(intent);

    //   // Validate receivedIntent and warn the user, if it is not correct,
    //   if (intent != null && intent.isNotNull) {
    //     setState(() {
    //       recieved
    //           .add(intent.data ?? intent.extra?["query"] as String? ?? "null");
    //     });
    //   }
    // }, onError: (err) {
    //   // Handle exception
    // });
  }

  Future<void> load() async {
    var result = await Channel.history;
    setState(() {
      recieved = result.history.map((element) {
        return "${element.action} :: ${element.flags.toRadixString(16).padLeft(8, '0')} :: ${element.data}";
      }).toList();
    });
  }

  @override
  void dispose() {
    _sub?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: ListView(
        children: <Widget>[
          ...recieved.map((v) => Text(v)),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: load,
        tooltip: 'Reload',
        child: const Icon(Icons.refresh),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
