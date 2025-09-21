import { useState } from "react";
import { Sidebar } from "./Sidebar";
import { NotesList } from "./NotesList";
import { NoteEditor } from "./NoteEditor";
import { AuthPage } from "./AuthPage";
import { PlusIcon } from "lucide-react";
import { Button } from "./ui/button";

interface Note {
  id: string;
  title: string;
  content: string;
  tags: string[];
  visibility: "PRIVATE" | "SHARED" | "PUBLIC";
  updatedAt: string;
  createdAt: string;
}

export const NotesApp = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentNote, setCurrentNote] = useState<Note | null>(null);
  const [notes, setNotes] = useState<Note[]>([
    {
      id: "1",
      title: "Welcome to Notes Suite",
      content: "# Welcome to Notes Suite\n\nThis is your modern note-taking application with **Markdown support**.\n\n## Features\n- Create and edit notes\n- Organize with tags\n- Share with others\n- Offline-first mobile app\n\nStart creating your first note!",
      tags: ["welcome", "getting-started"],
      visibility: "PRIVATE",
      updatedAt: new Date().toISOString(),
      createdAt: new Date().toISOString(),
    },
    {
      id: "2", 
      title: "Project Ideas",
      content: "# Project Ideas\n\n## Web Development\n- [ ] Personal portfolio\n- [ ] E-commerce platform\n- [ ] Social media app\n\n## Mobile Apps\n- [ ] Fitness tracker\n- [ ] Recipe organizer\n- [ ] Language learning app",
      tags: ["projects", "ideas", "development"],
      visibility: "PRIVATE",
      updatedAt: new Date(Date.now() - 86400000).toISOString(),
      createdAt: new Date(Date.now() - 86400000).toISOString(),
    }
  ]);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [visibilityFilter, setVisibilityFilter] = useState<string>("all");

  if (!isAuthenticated) {
    return <AuthPage onAuth={() => setIsAuthenticated(true)} />;
  }

  const createNewNote = () => {
    const newNote: Note = {
      id: Date.now().toString(),
      title: "Untitled Note",
      content: "# New Note\n\nStart writing...",
      tags: [],
      visibility: "PRIVATE",
      updatedAt: new Date().toISOString(),
      createdAt: new Date().toISOString(),
    };
    setNotes([newNote, ...notes]);
    setCurrentNote(newNote);
  };

  const updateNote = (updatedNote: Note) => {
    setNotes(notes.map(note => 
      note.id === updatedNote.id 
        ? { ...updatedNote, updatedAt: new Date().toISOString() }
        : note
    ));
    setCurrentNote(updatedNote);
  };

  const deleteNote = (noteId: string) => {
    setNotes(notes.filter(note => note.id !== noteId));
    if (currentNote?.id === noteId) {
      setCurrentNote(null);
    }
  };

  const filteredNotes = notes.filter(note => {
    const matchesSearch = note.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         note.content.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesTags = selectedTags.length === 0 || 
                       selectedTags.some(tag => note.tags.includes(tag));
    const matchesVisibility = visibilityFilter === "all" || 
                             note.visibility.toLowerCase() === visibilityFilter.toLowerCase();
    
    return matchesSearch && matchesTags && matchesVisibility;
  });

  return (
    <div className="flex h-screen bg-background relative overflow-hidden">
      {/* Background Effects */}
      <div className="absolute inset-0 bg-gradient-glow opacity-30" />
      <div className="absolute top-20 right-20 w-72 h-72 bg-primary/5 rounded-full blur-3xl animate-pulse" />
      <div className="absolute bottom-32 left-32 w-56 h-56 bg-accent/5 rounded-full blur-3xl animate-pulse delay-1000" />
      
      <Sidebar
        onLogout={() => setIsAuthenticated(false)}
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
        selectedTags={selectedTags}
        onTagsChange={setSelectedTags}
        visibilityFilter={visibilityFilter}
        onVisibilityChange={setVisibilityFilter}
        allTags={Array.from(new Set(notes.flatMap(note => note.tags)))}
      />
      
      <div className="flex flex-1 overflow-hidden relative z-10">
        <div className="w-80 border-r border-border bg-sidebar/80 backdrop-blur-sm">
          <div className="p-4 border-b border-border bg-gradient-card">
            <Button 
              onClick={createNewNote}
              className="w-full bg-gradient-primary hover:opacity-90 hover:scale-[1.02] active:scale-[0.98] transition-all duration-200 shadow-glow hover:shadow-floating text-white font-medium"
            >
              <PlusIcon className="w-4 h-4 mr-2" />
              New Note
            </Button>
          </div>
          
          <NotesList 
            notes={filteredNotes}
            currentNote={currentNote}
            onSelectNote={setCurrentNote}
            onDeleteNote={deleteNote}
          />
        </div>
        
        <div className="flex-1">
          {currentNote ? (
            <NoteEditor 
              note={currentNote}
              onUpdateNote={updateNote}
            />
          ) : (
            <div className="flex items-center justify-center h-full bg-gradient-card relative overflow-hidden">
              {/* Background decoration */}
              <div className="absolute top-1/4 left-1/4 w-32 h-32 bg-primary/5 rounded-full blur-2xl animate-pulse" />
              <div className="absolute bottom-1/3 right-1/3 w-24 h-24 bg-accent/5 rounded-full blur-2xl animate-pulse delay-500" />
              
              <div className="text-center relative z-10 animate-fade-in">
                <div className="w-28 h-28 mx-auto mb-6 bg-gradient-primary rounded-3xl flex items-center justify-center shadow-floating hover:scale-110 transition-all duration-300 animate-slide-up">
                  <PlusIcon className="w-10 h-10 text-primary-foreground" />
                </div>
                <h2 className="text-xl font-bold mb-3 bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent">
                  Ready to start?
                </h2>
                <p className="text-muted-foreground mb-8 max-w-md leading-relaxed">
                  Your digital workspace awaits.
                </p>
                <Button 
                  onClick={createNewNote}
                  className="bg-gradient-primary hover:opacity-90 hover:scale-[1.05] active:scale-[0.95] transition-all duration-200 shadow-glow hover:shadow-floating text-white font-medium px-8 py-3"
                >
                  <PlusIcon className="w-4 h-4 mr-2" />
                  Start Writing
                </Button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};