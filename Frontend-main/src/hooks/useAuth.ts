// hooks/useAuth.ts
import React, { useState, useContext, createContext, ReactNode, useEffect } from 'react';
import { apiService, LoginCredentials } from '../lib/apiService';

interface User {
    id: string;
    username: string;
    email?: string;
}

interface AuthContextType {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    login: (credentials: LoginCredentials) => Promise<boolean>;
    logout: () => void;
    loading: boolean;
    error: string | null;
}

// Créer le contexte avec undefined par défaut
const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
    children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps): React.JSX.Element {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const isAuthenticated: boolean = Boolean(user && token);

    useEffect(() => {
        const verifyToken = async (): Promise<void> => {
            const storedToken = localStorage.getItem('token');
            if (storedToken) {
                try {
                    const response = await apiService.verifyToken();
                    if (response.valid && response.user) {
                        setUser(response.user);
                        setToken(storedToken);
                    } else {
                        localStorage.removeItem('token');
                        setToken(null);
                    }
                } catch (error) {
                    localStorage.removeItem('token');
                    setToken(null);
                }
            }
        };
        
        verifyToken();
    }, []);

    const login = async (credentials: LoginCredentials): Promise<boolean> => {
        try {
            setLoading(true);
            setError(null);
            const response = await apiService.login(credentials);
            
            setUser(response.user);
            setToken(response.token);
            localStorage.setItem('token', response.token);
            return true;
        } catch (error) {
            setError('Identifiants invalides');
            console.error('Erreur de connexion:', error);
            return false;
        } finally {
            setLoading(false);
        }
    };

    const logout = (): void => {
        setUser(null);
        setToken(null);
        setError(null);
        localStorage.removeItem('token');
    };

    const contextValue: AuthContextType = {
        user,
        token,
        isAuthenticated,
        login,
        logout,
        loading,
        error
    };

    return React.createElement(
        AuthContext.Provider,
        { value: contextValue },
        children
    );
}

export function useAuth(): AuthContextType {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}
