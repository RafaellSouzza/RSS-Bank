import React from 'react';

function UserSidebar({ nome, numeroConta }) {
  return (
    <div className="w-1/3 bg-zinc-900 text-white p-8 flex flex-col justify-between">
      <div className="text-center">
        <img 
          src="https://lh3.googleusercontent.com/a/ACg8ocLAhmhcSDr502B1HjckZZYo8I7BBcrsoM6cmrTeg6Q2zsNZcUsqlA=s288-c-no" 
          alt="User" 
          className="w-40 h-40 rounded-full mx-auto mb-4"
        />
        <h1 className="text-xl font-bold">{nome}</h1>
        <p className="text-gray-300">Conta: {numeroConta}</p>
      </div>

      <div className="mt-8">
        <button className="w-full bg-zinc-700 py-2 px-4 rounded-lg text-center hover:bg-zinc-600 transition duration-300">
          Editar Perfil
        </button>
      </div>
    </div>
  );
}

export default UserSidebar;
