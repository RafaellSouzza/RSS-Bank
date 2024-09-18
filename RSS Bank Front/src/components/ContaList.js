import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { removerConta } from '../features/contasSlice';

function ContaList() {
  const contas = useSelector((state) => state.contas.contas);
  const dispatch = useDispatch();

  return (
    <div className="p-4 bg-white rounded shadow-md">
      <h2 className="text-xl font-bold mb-4">Contas Bancárias</h2>
      <ul>
        {contas.map((conta) => (
          <li key={conta.id} className="flex justify-between items-center mb-2">
            <span>{conta.numero}</span>
            <button
              onClick={() => dispatch(removerConta(conta.id))}
              className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
            >
              Remover
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ContaList;
