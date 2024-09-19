import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { realizarTransacao, removerTransacao } from '../features/transacoesSlice';

function Transacoes() {
  const [contaOrigem, setContaOrigem] = useState('');
  const [contaDestino, setContaDestino] = useState('');
  const [valor, setValor] = useState('');

  const dispatch = useDispatch();
  const transacoes = useSelector((state) => state.transacoes.transacoes);

  const handleSubmit = (e) => {
    e.preventDefault();
    const novaTransacao = {
      id: Date.now(),
      contaOrigem,
      contaDestino,
      valor: parseFloat(valor),
      data: new Date().toISOString(),
    };
    dispatch(realizarTransacao(novaTransacao));
    setContaOrigem('');
    setContaDestino('');
    setValor('');
  };

  const handleRemover = (id) => {
    dispatch(removerTransacao(id));
  };

  return (
    <div>
      <h1>Realizar Transações</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={contaOrigem}
          onChange={(e) => setContaOrigem(e.target.value)}
          placeholder="Conta de Origem"
          required
        />
        <input
          type="text"
          value={contaDestino}
          onChange={(e) => setContaDestino(e.target.value)}
          placeholder="Conta de Destino"
          required
        />
        <input
          type="number"
          value={valor}
          onChange={(e) => setValor(e.target.value)}
          placeholder="Valor da Transação"
          required
        />
        <button type="submit">Realizar Transação</button>
      </form>

      <h2>Transações Recentes</h2>
      <ul>
        {transacoes.map((transacao) => (
          <li key={transacao.id}>
            {transacao.contaOrigem} → {transacao.contaDestino}: R${transacao.valor}
            <button onClick={() => handleRemover(transacao.id)}>Remover</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Transacoes;
