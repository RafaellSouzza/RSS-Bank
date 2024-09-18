import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Contas from './pages/Contas';
import Transacoes from './pages/Transacoes';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/contas" element={<Contas />} />
        <Route path="/transacoes" element={<Transacoes />} />
      </Routes>
    </Router>
  );
}

export default App;
