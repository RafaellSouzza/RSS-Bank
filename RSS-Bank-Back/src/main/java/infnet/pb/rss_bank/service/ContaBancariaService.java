package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.TipoConta;
import infnet.pb.rss_bank.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ContaBancariaService {

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    public List<ContaBancaria> buscarContasPorCliente(UUID clienteId) {
        return contaBancariaRepository.findByClienteId(clienteId);
    }

    public ContaBancaria buscarContaPorNumero(String numeroConta) {
        return contaBancariaRepository.findByNumeroConta(numeroConta);
    }

    public List<ContaBancaria> buscarContasPorTipo(String tipoConta) {
        TipoConta tipoContaEnum = TipoConta.valueOf(tipoConta.toUpperCase());
        return contaBancariaRepository.findByTipoConta(tipoContaEnum);
    }

    public void atualizarSaldoConta(ContaBancaria conta, BigDecimal valor) {
        conta.setSaldo(conta.getSaldo().add(valor));
        contaBancariaRepository.save(conta);
    }

    public BigDecimal verificarSaldo(UUID contaId) {
        ContaBancaria conta = buscarContaPorId(contaId);
        return conta.getSaldo();
    }

    public ContaBancaria atualizarConta(ContaBancaria conta) {
        return contaBancariaRepository.save(conta);
    }

    public ContaBancaria buscarContaPorId(UUID contaId) {
        ContaBancaria conta = contaBancariaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));
        return conta ;
    }

    public void realizarSaque(UUID contaId, BigDecimal valor) {
        ContaBancaria conta = buscarContaPorId(contaId);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaBancariaRepository.save(conta);
    }

    public void realizarDeposito(UUID contaId, BigDecimal valor) {
        ContaBancaria conta = buscarContaPorId(contaId);
        conta.setSaldo(conta.getSaldo().add(valor));
        contaBancariaRepository.save(conta);
    }

    public void realizarTransferencia(UUID contaOrigemId, UUID contaDestinoId, BigDecimal valor) {
        ContaBancaria contaOrigem = buscarContaPorId(contaOrigemId);
        ContaBancaria contaDestino = buscarContaPorId(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);
    }
}
