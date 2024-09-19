import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav>
      <ul>
        <li><Link to="/">Início</Link></li>
        <li><Link to="/contas">Contas</Link></li>
        <li><Link to="/transacoes">Transações</Link></li>
        <li><Link to="/pagamento">Pagamento</Link></li>
      </ul>
    </nav>
  );
}

export default Navbar;
