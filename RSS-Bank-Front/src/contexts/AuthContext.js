// src/contexts/AuthContext.js
import React, { createContext, useState, useEffect } from 'react';
import api from '../api';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);

  useEffect(() => {
     console.log('AuthProvider useEffect iniciado');
 
     const token = localStorage.getItem('token');
     console.log('Token encontrado:', token);
  
    if (token) {
      api.get('/auth/me')
        .then((response) => {
          setUsuario(response.data);
        })
        .catch((error) => {
          console.error('Erro ao obter dados do usuário:', error);
          localStorage.removeItem('token');
          setUsuario(null);
        });
    }
  }, []);

const login = async (username, senha) => {
  try {
    const response = await api.post('/api/auth/login', { username, senha });
    console.log('Resposta do login:', response.data);

    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      setUsuario({ username });
      console.log('Token armazenado com sucesso.');
    } else {
      console.error('Token não encontrado na resposta do login.');
    }
  } catch (error) {
    console.error('Erro no login:', error.response?.data || error.message);
    throw error;
  }
};


  const logout = () => {
    localStorage.removeItem('token');
    setUsuario(null);
  };

  return (
    <AuthContext.Provider value={{ usuario, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
