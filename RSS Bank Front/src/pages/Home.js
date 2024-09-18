import React from 'react';
import UserSidebar from '../components/UserSidebar';

function Home() {
  return (
    <div className="min-h-screen flex bg-gray-900 text-white">
      <UserSidebar nome="Rafael Soares" numeroConta="44.000" />

      <div className="w-2/3 p-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-4">Minha Conta</h2>
          <p className="text-xl">Saldo: R$ 915.356,98</p>
        </div>

        <div className="grid grid-cols-3 gap-4 mb-8">
          <button className="bg-gray-800 shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition">
            <p className="font-bold text-white">Pix</p>
          </button>
          <button className="bg-gray-800 shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition">
            <p className="font-bold text-white">Pagar</p>
          </button>
          <button className="bg-gray-800 shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition">
            <p className="font-bold text-white">Pegar Empréstimo</p>
          </button>
          <button className="bg-gray-800 shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition">
            <p className="font-bold text-white">Transferir</p>
          </button>
          <button className="bg-gray-800 shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition">
            <p className="font-bold text-white">Meus Cartões</p>
          </button>
        </div>

        {/* Informações adicionais */}
        <div className="bg-gray-800 shadow-lg p-6 rounded-lg mb-4">
          <h2 className="text-lg font-bold text-white mb-2">Novidade!</h2>
          <p className="text-gray-400">Conheça a ferramenta de Controle de Gastos do RSS Bank.</p>
        </div>

        <div className="bg-gray-800 shadow-lg p-6 rounded-lg">
          <h2 className="text-lg font-bold text-white mb-2">Cartão de Crédito</h2>
          <p className="text-gray-400">Fatura atual: R$ 500,00</p>
        </div>
      </div>
    </div>
  );
}

export default Home;
