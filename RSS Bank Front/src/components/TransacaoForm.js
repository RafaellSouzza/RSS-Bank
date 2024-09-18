import React, { useState } from 'react';
import api from '../services/api';

function TransacaoForm() {
  const [valor, setValor] = useState('');
  const [contaOrigem, setContaOrigem] = useState('');
  const [contaDestino, setContaDestino] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post('/transacoes', { valor, contaOrigem, contaDestino });
    alert('Transação realizada com sucesso');
  };

  return (
    <div>
      <h2>Nova Transação</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Conta Origem:</label>
          <input
            type="text"
            value={contaOrigem}
            onChange={(e) => setContaOrigem(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Conta Destino:</label>
          <input
            type="text"
            value={contaDestino}
            onChange={(e) => setContaDestino(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Valor:</label>
          <input
            type="number"
            value={valor}
            onChange={(e) => setValor(e.target.value)}
            required
          />
        </div>
        <button type="submit">Realizar Transação</button>
      </form>
    </div>
  );
}

export default TransacaoForm;
