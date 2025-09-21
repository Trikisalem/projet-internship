import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../data/database.dart';
import 'note_editor_screen.dart';

final notesProvider = FutureProvider<List<Map<String, dynamic>>>((ref) async {
  final dbService = DatabaseService();
  return await dbService.getNotes();
});

class NotesScreen extends ConsumerWidget {
  const NotesScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final notesAsync = ref.watch(notesProvider);
    final refreshController = RefreshController();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Notes'),
        actions: [
          IconButton(
            icon: const Icon(Icons.search),
            onPressed: () {
              // TODO: Implement search
            },
          ),
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () async {
              final prefs = await SharedPreferences.getInstance();
              await prefs.remove('auth_token');
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(builder: (context) => const AuthScreen()),
              );
            },
          ),
        ],
      ),
      body: notesAsync.when(
        data: (notes) => SmartRefresher(
          controller: refreshController,
          onRefresh: () async {
            await ref.refresh(notesProvider.future);
            refreshController.refreshCompleted();
          },
          child: notes.isEmpty
              ? const Center(
                  child: Text(
                    'No notes yet\nTap + to create your first note',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontSize: 18, color: Colors.grey),
                  ),
                )
              : ListView.builder(
                  itemCount: notes.length,
                  itemBuilder: (context, index) {
                    final note = notes[index];
                    return ListTile(
                      title: Text(note['title'] ?? 'Untitled'),
                      subtitle: Text(
                        note['content']?.toString().substring(
                                0,
                                (note['content']?.toString().length ?? 0) > 100
                                    ? 100
                                    : note['content']?.toString().length ?? 0,
                              ) ?
                              '...' :
                              '',
                      ),
                      trailing: note['is_synced'] == 0
                          ? const Icon(Icons.cloud_off, size: 16)
                          : null,
                      onTap: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (context) => NoteEditorScreen(note: note),
                          ),
                        );
                      },
                    );
                  },
                ),
        ),
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(child: Text('Error: $error')),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (context) => const NoteEditorScreen(),
            ),
          );
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}