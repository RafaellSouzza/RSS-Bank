// src/pages/Login.js
import React, { useState, useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [username, setUsername] = useState('');
  const [senha, setSenha] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const validateForm = () => {
    if (username.trim() === '' || senha.trim() === '') {
      setErrorMessage('Por favor, preencha todos os campos.');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) {
      return;
    }
    try {
      await login(username, senha);
      navigate('/');
    } catch (error) {
      console.error(error);
      setErrorMessage('Nome de usuário ou senha incorretos.');
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-darkBg">
      <div className="bg-darkCard p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-center mb-6 text-primary">Login</h1>
        {errorMessage && (
          <div className="bg-red-600 text-white px-4 py-2 mb-4 rounded">
            {errorMessage}
          </div>
        )}
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="username" className="block text-darkText mb-2">
              Usuário
            </label>
            <input
              type="text"
              id="username"
              className="w-full bg-darkBg border border-gray-700 px-3 py-2 rounded text-darkText focus:outline-none focus:border-primary"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Digite seu usuário"
              required
            />
          </div>
          <div className="mb-6">
            <label htmlFor="senha" className="block text-darkText mb-2">
              Senha
            </label>
            <input
              type="password"
              id="senha"
              className="w-full bg-darkBg border border-gray-700 px-3 py-2 rounded text-darkText focus:outline-none focus:border-primary"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              placeholder="Digite sua senha"
              required
            />
          </div>
          <button
            type="submit"
            className="w-full bg-primary text-darkBg py-2 rounded hover:bg-purple-700 transition duration-200"
          >
            Entrar
          </button>
        </form>
        <div className="mt-6 text-center">
          <p className="text-darkText">
            Não tem uma conta?{' '}
            <a href="/register" className="text-secondary hover:underline">
              Cadastre-se
            </a>
          </p>
          <p className="text-darkText mt-2">
            Esqueceu sua senha?{' '}
            <a href="/forgot-password" className="text-secondary hover:underline">
              Recupere aqui
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
