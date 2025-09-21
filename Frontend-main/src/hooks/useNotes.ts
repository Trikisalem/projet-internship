// hooks/useNotes.ts
import { useState, useEffect, useCallback } from 'react';
import { apiService, Note } from '../lib/apiService';

// Interface pour définir les types d'erreur
interface ApiError extends Error {
    status?: number;
    code?: string;
}

// Interface pour les données de création de note
interface CreateNoteData {
    title: string;
    content: string;
}

// Interface pour le retour du hook
interface UseNotesReturn {
    notes: Note[];
    loading: boolean;
    error: string | null;
    createNote: (noteData: CreateNoteData) => Promise<Note | null>;
    deleteNote: (id: string) => Promise<boolean>;
    refreshNotes: () => Promise<void>;
}

export function useNotes(): UseNotesReturn {
    const [notes, setNotes] = useState<Note[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const loadNotes = useCallback(async (): Promise<void> => {
        try {
            setLoading(true);
            setError(null);
            const data = await apiService.getNotes();
            setNotes(data);
        } catch (err) {
            // Type assertion avec vérification
            const errorMessage = err instanceof Error 
                ? err.message 
                : 'Erreur lors du chargement des notes';
            
            setError(errorMessage);
            console.error('Erreur chargement notes:', err);
        } finally {
            setLoading(false);
        }
    }, []);

    const createNote = async (noteData: CreateNoteData): Promise<Note | null> => {
        try {
            setError(null);
            const newNote = await apiService.createNote(noteData);
            setNotes(prev => [newNote, ...prev]);
            return newNote;
        } catch (err) {
            // Gestion d'erreur typée
            const errorMessage = err instanceof Error 
                ? err.message 
                : 'Erreur lors de la création de la note';
            
            setError(errorMessage);
            console.error('Erreur création note:', err);
            return null;
        }
    };

    const deleteNote = async (id: string): Promise<boolean> => {
        try {
            setError(null);
            await apiService.deleteNote(id);
            setNotes(prev => prev.filter(note => note.id !== id));
            return true;
        } catch (err) {
            // Type guard pour l'erreur
            let errorMessage = 'Erreur lors de la suppression';
            
            if (err instanceof Error) {
                errorMessage = err.message;
            } else if (typeof err === 'string') {
                errorMessage = err;
            }
            
            setError(errorMessage);
            console.error('Erreur suppression note:', err);
            return false;
        }
    };

    useEffect(() => {
        loadNotes();
    }, [loadNotes]);

    return { 
        notes, 
        loading, 
        error, 
        createNote, 
        deleteNote, 
        refreshNotes: loadNotes 
    };
}
