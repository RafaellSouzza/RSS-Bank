package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.TipoTransacao;
import infnet.pb.rss_bank.model.Transacao;
import infnet.pb.rss_bank.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaBancariaService contaBancariaService;

    public Transacao realizarSaque(UUID contaId, BigDecimal valor) {
        ContaBancaria conta = contaBancariaService.buscarContaPorId(contaId);
        contaBancariaService.realizarSaque(contaId, valor);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
        }

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.SAQUE);
        transacao.setContaOrigem(conta);
        transacao.setValor(valor);

        return transacaoRepository.save(transacao);
    }

    public Transacao realizarDeposito(UUID contaId, BigDecimal valor) {
        ContaBancaria conta = contaBancariaService.buscarContaPorId(contaId);
        contaBancariaService.realizarDeposito(contaId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setContaDestino(conta);
        transacao.setValor(valor);

        return transacaoRepository.save(transacao);
    }

    public Transacao realizarTransferencia(UUID contaOrigemId, UUID contaDestinoId, BigDecimal valor) {
        ContaBancaria contaOrigem = contaBancariaService.buscarContaPorId(contaOrigemId);
        ContaBancaria contaDestino = contaBancariaService.buscarContaPorId(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        contaBancariaService.realizarTransferencia(contaOrigemId, contaDestinoId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.TRANSFERENCIA);
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);

        return transacaoRepository.save(transacao);
    }


    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }

    public List<Transacao> buscarTransacoesPorContaOrigem(UUID contaOrigemId) {
        return transacaoRepository.findByContaOrigemId(contaOrigemId);
    }

    public List<Transacao> buscarTransacoesPorContaDestino(UUID contaDestinoId) {
        return transacaoRepository.findByContaDestinoId(contaDestinoId);
    }
}
