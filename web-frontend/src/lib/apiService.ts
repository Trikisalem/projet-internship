// lib/apiService.ts

// Interfaces pour les types
interface Note {
    id?: string;
    title: string;
    content: string;
    tags?: string[];
    createdAt?: string;
    updatedAt?: string;
}

interface LoginCredentials {
    username: string;
    password: string;
}

interface LoginResponse {
    token: string;
    user: {
        id: string;
        username: string;
        email?: string;
    };
}

interface ApiError {
    message: string;
    status: number;
    code?: string;
}

interface User {
    id: string;
    username: string;
    email?: string;
}

interface HealthResponse {
    status: string;
    timestamp: string;
}

interface SyncResponse {
    notes: Note[];
    timestamp: string;
}

interface ImportResponse {
    imported: number;
    errors: string[];
}

interface VerifyTokenResponse {
    valid: boolean;
    user?: User;
}

export class ApiService {
    private baseURL = 'http://localhost:8080/api';
    
    // Méthode générique pour les requêtes - SANS ANY
    async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
        const token = localStorage.getItem('token');
        
        const response = await fetch(`${this.baseURL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...(token && { Authorization: `Bearer ${token}` }),
                ...options.headers,
            },
            ...options,
        });
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({})) as { message?: string; code?: string };
            const error: ApiError = {
                message: errorData.message || `HTTP Error: ${response.status}`,
                status: response.status,
                code: errorData.code
            };
            throw error;
        }
        
        return response.json() as Promise<T>;
    }
    
    // === AUTHENTIFICATION ===
    async login(credentials: LoginCredentials): Promise<LoginResponse> {
        return this.request<LoginResponse>('/auth/login', {
            method: 'POST',
            body: JSON.stringify(credentials)
        });
    }
    
    async register(userData: LoginCredentials & { email?: string }): Promise<LoginResponse> {
        return this.request<LoginResponse>('/auth/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    }
    
    async logout(): Promise<void> {
        return this.request<void>('/auth/logout', {
            method: 'POST'
        });
    }
    
    async verifyToken(): Promise<VerifyTokenResponse> {
        return this.request<VerifyTokenResponse>('/auth/verify');
    }
    
    // === GESTION DES NOTES ===
    async getNotes(): Promise<Note[]> {
        return this.request<Note[]>('/notes');
    }
    
    async getNoteById(id: string): Promise<Note> {
        return this.request<Note>(`/notes/${id}`);
    }
    
    async createNote(note: Omit<Note, 'id' | 'createdAt' | 'updatedAt'>): Promise<Note> {
        return this.request<Note>('/notes', {
            method: 'POST',
            body: JSON.stringify(note)
        });
    }
    
    async updateNote(id: string, note: Partial<Omit<Note, 'id' | 'createdAt' | 'updatedAt'>>): Promise<Note> {
        return this.request<Note>(`/notes/${id}`, {
            method: 'PUT',
            body: JSON.stringify(note)
        });
    }
    
    async deleteNote(id: string): Promise<void> {
        return this.request<void>(`/notes/${id}`, {
            method: 'DELETE'
        });
    }
    
    // === RECHERCHE ET FILTRES ===
    async searchNotes(query: string): Promise<Note[]> {
        return this.request<Note[]>(`/notes/search?q=${encodeURIComponent(query)}`);
    }
    
    async getNotesByTag(tag: string): Promise<Note[]> {
        return this.request<Note[]>(`/notes/tag/${encodeURIComponent(tag)}`);
    }
    
    // === SYNCHRONISATION ===
    async syncNotes(lastSync?: string): Promise<SyncResponse> {
        const endpoint = lastSync 
            ? `/notes/sync?since=${encodeURIComponent(lastSync)}`
            : '/notes/sync';
        return this.request<SyncResponse>(endpoint);
    }
    
    // === EXPORT/IMPORT ===
    async exportNotes(format: 'json' | 'markdown' = 'json'): Promise<Blob> {
        const token = localStorage.getItem('token');
        const response = await fetch(`${this.baseURL}/notes/export?format=${format}`, {
            headers: {
                ...(token && { Authorization: `Bearer ${token}` })
            }
        });
        
        if (!response.ok) {
            throw new Error(`Export failed: ${response.status}`);
        }
        
        return response.blob();
    }
    
    async importNotes(file: File): Promise<ImportResponse> {
        const formData = new FormData();
        formData.append('file', file);
        
        const token = localStorage.getItem('token');
        const response = await fetch(`${this.baseURL}/notes/import`, {
            method: 'POST',
            headers: {
                ...(token && { Authorization: `Bearer ${token}` })
            },
            body: formData
        });
        
        if (!response.ok) {
            throw new Error(`Import failed: ${response.status}`);
        }
        
        return response.json() as Promise<ImportResponse>;
    }
    
    // === UTILITAIRES ===
    async checkHealth(): Promise<HealthResponse> {
        return this.request<HealthResponse>('/health');
    }
    
    // Configuration de l'URL de base
    setBaseURL(url: string): void {
        this.baseURL = url.endsWith('/') ? url.slice(0, -1) : url;
    }
    
    getBaseURL(): string {
        return this.baseURL;
    }
}

// Instance singleton
export const apiService = new ApiService();

// Types exportés pour utilisation dans les composants
export type { 
    Note, 
    LoginCredentials, 
    LoginResponse, 
    ApiError, 
    User, 
    HealthResponse, 
    SyncResponse, 
    ImportResponse, 
    VerifyTokenResponse 
};
