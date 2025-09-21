import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'auth/auth_screen.dart';
import 'notes/notes_screen.dart';

class NotesApp extends StatelessWidget {
  const NotesApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Notes App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: Consumer(
        builder: (context, ref, child) {
          final authState = ref.watch(authProvider);
          return authState.when(
            data: (isLoggedIn) => isLoggedIn ? const NotesScreen() : const AuthScreen(),
            loading: () => const Scaffold(body: Center(child: CircularProgressIndicator())),
            error: (error, stack) => Scaffold(body: Center(child: Text('Error: $error'))),
          );
        },
      ),
    );
  }
}

final authProvider = FutureProvider<bool>((ref) async {
  final prefs = await SharedPreferences.getInstance();
  return prefs.getString('auth_token') != null;
});