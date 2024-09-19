import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { adicionarConta } from '../features/contasSlice';

function ContaForm() {
  const [numero, setNumero] = useState('');
  const dispatch = useDispatch();

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(adicionarConta({ id: Date.now(), numero }));
    setNumero('');
  };

  return (
    <form onSubmit={handleSubmit} className="p-4 bg-white rounded shadow-md">
      <div className="mb-4">
        <label className="block text-gray-700">Número da Conta</label>
        <input
          type="text"
          value={numero}
          onChange={(e) => setNumero(e.target.value)}
          className="border p-2 rounded w-full"
          required
        />
      </div>
      <button
        type="submit"
        className="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
      >
        Adicionar Conta
      </button>
    </form>
  );
}

export default ContaForm;
