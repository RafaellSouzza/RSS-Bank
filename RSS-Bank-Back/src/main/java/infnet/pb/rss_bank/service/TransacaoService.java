package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.StatusTransacao;
import infnet.pb.rss_bank.model.TipoTransacao;
import infnet.pb.rss_bank.model.Transacao;
import infnet.pb.rss_bank.repository.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String AUDITORIA_EXCHANGE = "auditoria.exchange";
    private static final String AUDITORIA_ROUTING_KEY = "auditoria.routingkey";

    @Transactional
    public Transacao realizarSaque(UUID contaOrigemId, BigDecimal valor) {
        ContaBancaria contaOrigem = contaBancariaService.buscarContaPorId(contaOrigemId);
        if (contaOrigem == null) {
            throw new EntityNotFoundException("Conta de origem não encontrada");
        }

        contaBancariaService.realizarSaque(contaOrigemId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.SAQUE);
        transacao.setValor(valor);
        transacao.setContaOrigem(contaOrigem);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao("Saque de " + valor + " realizado.");

        String usuarioResponsavel = contaOrigem.getCliente().getNome();
        auditoriaService.registrarAuditoria("SAQUE", "Saque", contaOrigemId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Saque realizado por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao realizarDeposito(UUID contaDestinoId, BigDecimal valor) {
        ContaBancaria contaDestino = contaBancariaService.buscarContaPorId(contaDestinoId);
        if (contaDestino == null) {
            throw new EntityNotFoundException("Conta de destino não encontrada");
        }

        contaBancariaService.realizarDeposito(contaDestinoId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.DEPOSITO);
        transacao.setValor(valor);
        transacao.setContaDestino(contaDestino);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao("Depósito de " + valor + " realizado.");

        String usuarioResponsavel = contaDestino.getCliente().getNome();
        auditoriaService.registrarAuditoria("DEPOSITO", "Depósito", contaDestinoId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Depósito realizado por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao realizarTransferencia(UUID contaOrigemId, UUID contaDestinoId, BigDecimal valor) {
        ContaBancaria contaOrigem = contaBancariaService.buscarContaPorId(contaOrigemId);
        ContaBancaria contaDestino = contaBancariaService.buscarContaPorId(contaDestinoId);

        if (contaOrigem == null) {
            throw new EntityNotFoundException("Conta de origem não encontrada");
        }
        if (contaDestino == null) {
            throw new EntityNotFoundException("Conta de destino não encontrada");
        }

        contaBancariaService.realizarTransferencia(contaOrigemId, contaDestinoId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.TRANSFERENCIA);
        transacao.setValor(valor);
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao("Transferência de " + valor + " para conta de destino " + contaDestino.getNumeroConta());

        String usuarioResponsavel = contaOrigem.getCliente().getNome();
        auditoriaService.registrarAuditoria("TRANSFERENCIA", "Transferência", contaOrigemId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Transferência realizada por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao realizarPix(UUID contaOrigemId, String contaDestinoId, BigDecimal valor) {
        ContaBancaria contaOrigem = contaBancariaService.buscarContaPorId(contaOrigemId);
        if (contaOrigem == null) {
            throw new EntityNotFoundException("Conta de origem não encontrada");
        }

        ContaBancaria contaDestino = contaBancariaService.buscarContaPorNumero(contaDestinoId);
        if (contaDestino == null) {
            throw new EntityNotFoundException("Conta de destino não encontrada");
        }

        contaBancariaService.realizarTransferencia(contaOrigemId, contaDestino.getId(), valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.PIX);
        transacao.setValor(valor);
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao("Transação Pix de " + valor + " para conta de destino " + contaDestino.getNumeroConta());

        String usuarioResponsavel = contaOrigem.getCliente().getNome();
        auditoriaService.registrarAuditoria("PIX", "Pix", contaOrigemId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Pix realizado por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao realizarPagamento(UUID contaOrigemId, BigDecimal valor, String descricao) {
        ContaBancaria contaOrigem = contaBancariaService.buscarContaPorId(contaOrigemId);
        if (contaOrigem == null) {
            throw new EntityNotFoundException("Conta de origem não encontrada");
        }

        contaBancariaService.realizarSaque(contaOrigemId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.PAGAMENTO);
        transacao.setValor(valor);
        transacao.setContaOrigem(contaOrigem);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao(descricao);

        String usuarioResponsavel = contaOrigem.getCliente().getNome();
        auditoriaService.registrarAuditoria("PAGAMENTO", "Pagamento", contaOrigemId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Pagamento realizado por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao realizarEmprestimo(UUID contaDestinoId, BigDecimal valor) {
        ContaBancaria contaDestino = contaBancariaService.buscarContaPorId(contaDestinoId);
        if (contaDestino == null) {
            throw new EntityNotFoundException("Conta de destino não encontrada");
        }

        contaBancariaService.realizarDeposito(contaDestinoId, valor);

        Transacao transacao = new Transacao();
        transacao.setData(LocalDateTime.now());
        transacao.setTipo(TipoTransacao.EMPRESTIMO);
        transacao.setValor(valor);
        transacao.setContaDestino(contaDestino);
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacao.setDescricao("Empréstimo de " + valor + " realizado para a conta " + contaDestino.getNumeroConta());

        String usuarioResponsavel = contaDestino.getCliente().getNome();
        auditoriaService.registrarAuditoria("EMPRESTIMO", "Empréstimo", contaDestinoId, valor, usuarioResponsavel);

        rabbitTemplate.convertAndSend(AUDITORIA_EXCHANGE, AUDITORIA_ROUTING_KEY, "Empréstimo realizado por " + usuarioResponsavel);

        return transacaoRepository.save(transacao);
    }

    public List<Transacao> obterExtrato(UUID contaId) {
        return transacaoRepository.findByContaOrigemIdOrContaDestinoId(contaId, contaId);
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
