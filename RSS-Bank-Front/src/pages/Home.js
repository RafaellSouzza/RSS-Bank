import React, { useEffect, useState } from 'react';
import UserSidebar from '../components/UserSidebar';
import Modal from '../components/Modal';

function Home() {
  const [clientData, setClientData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [transacaoTipo, setTransacaoTipo] = useState('');
  const [formData, setFormData] = useState({});
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [mensagemErro, setMensagemErro] = useState('');
  const [saldo, setSaldo] = useState(0);
  const [extratoTransacoes, setExtratoTransacoes] = useState([]);

  const abrirModal = (tipo) => {
    setTransacaoTipo(tipo);
    setIsModalOpen(true);
    setFormData({});
    setMensagemSucesso('');
    setMensagemErro('');
    setExtratoTransacoes([]);
  };

  const fecharModal = () => {
    setIsModalOpen(false);
    setMensagemSucesso('');
    setMensagemErro('');
    setTransacaoTipo('');
    setExtratoTransacoes([]);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensagemSucesso('');
    setMensagemErro('');

    try {
      let endpoint = '';
      const contaOrigemId = clientData.contas[0].id;
      let queryString = `?contaOrigemId=${contaOrigemId}&valor=${parseFloat(formData.valor)}`;

      switch (transacaoTipo) {
        case 'pix':
          queryString += `&contaDestinoId=${formData.chavePixDestino}`;
          endpoint = '/api/transacoes/pix';
          break;
        case 'pagamento':
          queryString += `&descricao=${formData.descricao}`;
          endpoint = '/api/transacoes/pagamento';
          break;
        case 'transferencia':
          queryString += `&contaDestinoId=${formData.contaDestinoId}`;
          endpoint = '/api/transacoes/transferencia';
          break;
        case 'emprestimo':
          queryString += `&contaDestinoId=${formData.contaDestinoId}`;
          endpoint = '/api/transacoes/emprestimo';
          break;
        case 'deposito':
          queryString += `&contaDestinoId=${formData.contaDestinoId}`;
          endpoint = '/api/transacoes/deposito';
          break;
        default:
          throw new Error('Tipo de transação inválido');
      }

      const response = await fetch(`http://localhost:8080${endpoint}${queryString}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Erro ao realizar a transação');
      }

      if (transacaoTipo !== 'extrato') {
        setMensagemSucesso('Transação realizada com sucesso!');
        if (['saque', 'pagamento', 'pix', 'transferencia'].includes(transacaoTipo)) {
          setSaldo(prev => prev - parseFloat(formData.valor));
        } else if (['deposito', 'emprestimo'].includes(transacaoTipo)) {
          setSaldo(prev => prev + parseFloat(formData.valor));
        }
        setTimeout(() => {
          fecharModal();
        }, 2000);
      }
    } catch (err) {
      setMensagemErro(err.message || 'Erro ao realizar a transação');
    }
  };

  const handleExtrato = async (e) => {
    e.preventDefault();
    setMensagemSucesso('');
    setMensagemErro('');
    setExtratoTransacoes([]);

    try {
      const contaId = clientData.contas[0].id;

      const response = await fetch(`http://localhost:8080/api/transacoes/extrato/${contaId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Erro ao obter o extrato');
      }

      const data = await response.json();
      setExtratoTransacoes(data);
      setMensagemSucesso('Extrato obtido com sucesso!');
    } catch (err) {
      setMensagemErro(err.message || 'Erro ao obter o extrato');
    }
  };

  useEffect(() => {
    const fetchClientData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/usuario/me', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Erro na requisição: ${response.status} ${response.statusText} - ${errorText}`);
        }

        const data = await response.json();
        setClientData(data);

        const contas = data.contas;
        if (contas && contas.length > 0) {
          setSaldo(contas[0].saldo);
        }
      } catch (error) {
        console.error('Erro ao buscar dados do cliente:', error);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchClientData();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-darkBg text-darkText font-sans">
        <p>Carregando...</p>
      </div>
    );
  }

  if (error || !clientData) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-darkBg text-darkText font-sans">
        <p>Erro ao carregar dados. Por favor, tente novamente mais tarde.</p>
      </div>
    );
  }

  const nomeUsuario = clientData.nome;
  const contas = clientData.contas;

  if (!contas || contas.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-darkBg text-darkText font-sans">
        <p>Nenhuma conta encontrada para este cliente.</p>
      </div>
    );
  }

  const conta = contas[0];
  const numeroConta = conta.numeroConta;

  return (
    <div className="min-h-screen flex bg-darkBg text-darkText font-sans">
      <UserSidebar nome={nomeUsuario} numeroConta={numeroConta} />

      <div className="w-full p-8">
        <div className="mb-8">
          <h2 className="text-4xl font-bold mb-4 text-primary">Minha Conta</h2>
          <p className="text-2xl">Saldo: R$ {saldo?.toFixed(2)}</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 mb-8">
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('pix')}
          >
            <p className="font-bold text-darkText">Pix</p>
          </button>
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('pagamento')}
          >
            <p className="font-bold text-darkText">Pagar</p>
          </button>
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('emprestimo')}
          >
            <p className="font-bold text-darkText">Pegar Empréstimo</p>
          </button>
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('transferencia')}
          >
            <p className="font-bold text-darkText">Transferir</p>
          </button>
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('deposito')}
          >
            <p className="font-bold text-darkText">Depósito</p>
          </button>
          <button
            className="bg-darkCard shadow-lg p-4 rounded-lg text-center hover:bg-gray-700 transition duration-200"
            onClick={() => abrirModal('extrato')}
          >
            <p className="font-bold text-darkText">Extrato</p>
          </button>
        </div>

        <div className="bg-darkCard shadow-lg p-6 rounded-lg mb-4">
          <h2 className="text-xl font-bold text-primary mb-2">Novidade!</h2>
          <p className="text-gray-400">Conheça a ferramenta de Controle de Gastos do RSS Bank.</p>
        </div>
      </div>

      <Modal isOpen={isModalOpen} onClose={fecharModal}>
        {mensagemSucesso ? (
          <div className="text-center">
            <h2 className="text-2xl font-bold mb-4">{mensagemSucesso}</h2>
          </div>
        ) : (
          <>
            <h2 className="text-2xl font-bold mb-4">
              {transacaoTipo === 'pix' && 'Enviar Pix'}
              {transacaoTipo === 'pagamento' && 'Efetuar Pagamento'}
              {transacaoTipo === 'transferencia' && 'Transferir'}
              {transacaoTipo === 'emprestimo' && 'Pegar Empréstimo'}
              {transacaoTipo === 'deposito' && 'Realizar Depósito'}
              {transacaoTipo === 'extrato' && 'Consultar Extrato'}
            </h2>

            {transacaoTipo === 'extrato' ? (
              <ExtratoForm 
                formData={formData} 
                handleChange={handleChange} 
                handleSubmit={handleExtrato} 
                fecharModal={fecharModal}
              />
            ) : (
              <form onSubmit={handleSubmit}>
                {transacaoTipo === 'pix' && (
                  <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Chave Pix Destino:</label>
                    <input
                      type="text"
                      name="chavePixDestino"
                      className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                      value={formData.chavePixDestino || ''}
                      onChange={handleChange}
                      required
                    />
                  </div>
                )}

                {transacaoTipo === 'pagamento' && (
                  <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Código de Barras:</label>
                    <input
                      type="text"
                      name="codigoBarras"
                      className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                      value={formData.codigoBarras || ''}
                      onChange={handleChange}
                      required
                    />
                  </div>
                )}

                {transacaoTipo === 'transferencia' && (
                  <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Conta Destino ID:</label>
                    <input
                      type="text"
                      name="contaDestinoId"
                      className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                      value={formData.contaDestinoId || ''}
                      onChange={handleChange}
                      required
                    />
                  </div>
                )}

                {transacaoTipo === 'emprestimo' && (
                  <>
                    <div className="mb-4">
                      <label className="block text-sm font-medium mb-1">Prazo em Meses:</label>
                      <input
                        type="number"
                        name="prazoEmMeses"
                        className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                        value={formData.prazoEmMeses || ''}
                        onChange={handleChange}
                        required
                      />
                    </div>
                    <div className="mb-4">
                      <label className="block text-sm font-medium mb-1">Taxa de Juros (%):</label>
                      <input
                        type="number"
                        step="0.01"
                        name="taxaJuros"
                        className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                        value={formData.taxaJuros || ''}
                        onChange={handleChange}
                        required
                      />
                    </div>
                  </>
                )}

                {transacaoTipo === 'deposito' && (
                  <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Conta Destino ID:</label>
                    <input
                      type="text"
                      name="contaDestinoId"
                      className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                      value={formData.contaDestinoId || ''}
                      onChange={handleChange}
                      required
                    />
                  </div>
                )}

                {transacaoTipo !== 'extrato' && (
                  <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Valor:</label>
                    <input
                      type="number"
                      name="valor"
                      step="0.01"
                      min="0"
                      className="w-full border border-gray-300 rounded px-3 py-2 text-black"
                      value={formData.valor || ''}
                      onChange={handleChange}
                      required
                    />
                  </div>
                )}

                {transacaoTipo !== 'extrato' && (
                  <div className="flex justify-end">
                    <button
                      type="button"
                      className="mr-4 px-4 py-2"
                      onClick={fecharModal}
                    >
                      Cancelar
                    </button>
                    <button type="submit" className="bg-primary text-white px-4 py-2 rounded">
                      {transacaoTipo === 'emprestimo' ? 'Solicitar' : 'Enviar'}
                    </button>
                  </div>
                )}
              </form>
            )}

            {mensagemErro && (
              <div className="mt-4 text-red-500">
                <p>{mensagemErro}</p>
              </div>
            )}

            {transacaoTipo === 'extrato' && extratoTransacoes.length > 0 && (
              <div className="mt-4">
                <h3 className="text-xl font-bold mb-2">Extrato</h3>
                <table className="min-w-full bg-white">
                  <thead>
                    <tr>
                      <th className="py-2 px-4 border-b">ID</th>
                      <th className="py-2 px-4 border-b">Tipo</th>
                      <th className="py-2 px-4 border-b">Valor</th>
                      <th className="py-2 px-4 border-b">Data</th>
                      <th className="py-2 px-4 border-b">Conta Origem</th>
                      <th className="py-2 px-4 border-b">Conta Destino</th>
                    </tr>
                  </thead>
                  <tbody>
                    {extratoTransacoes.map(transacao => (
                      <tr key={transacao.id}>
                        <td className="py-2 px-4 border-b">{transacao.id}</td>
                        <td className="py-2 px-4 border-b">{transacao.tipo}</td>
                        <td className="py-2 px-4 border-b">R$ {transacao.valor.toFixed(2)}</td>
                        <td className="py-2 px-4 border-b">{new Date(transacao.data).toLocaleString()}</td>
                        <td className="py-2 px-4 border-b">{transacao.contaOrigem ? transacao.contaOrigem.id : '-'}</td>
                        <td className="py-2 px-4 border-b">{transacao.contaDestino ? transacao.contaDestino.id : '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </>
        )}
      </Modal>
    </div>
  );
}

function ExtratoForm({ formData, handleChange, handleSubmit, fecharModal }) {
  return (
    <form onSubmit={handleSubmit}>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Conta ID:</label>
        <input
          type="text"
          name="contaId"
          className="w-full border border-gray-300 rounded px-3 py-2 text-black"
          value={formData.contaId || ''}
          onChange={handleChange}
          required
        />
      </div>
      <div className="flex justify-end">
        <button
          type="button"
          className="mr-4 px-4 py-2"
          onClick={fecharModal}
        >
          Cancelar
        </button>
        <button type="submit" className="bg-primary text-white px-4 py-2 rounded">
          Consultar
        </button>
      </div>
    </form>
  );
}

export default Home;
