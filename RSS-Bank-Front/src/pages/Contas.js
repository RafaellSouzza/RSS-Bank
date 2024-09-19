import React from 'react';
import ContaForm from '../components/ContaForm';
import ContaList from '../components/ContaList';

function Contas() {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">Gerenciamento de Contas</h1>
      <ContaForm />
      <ContaList />
    </div>
  );
}

export default Contas;
