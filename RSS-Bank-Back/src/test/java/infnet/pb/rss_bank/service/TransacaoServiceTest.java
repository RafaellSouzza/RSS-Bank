package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.TipoTransacao;
import infnet.pb.rss_bank.model.Transacao;
import infnet.pb.rss_bank.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaBancariaService contaBancariaService;

    @InjectMocks
    private TransacaoService transacaoService;

    private ContaBancaria contaOrigem;
    private ContaBancaria contaDestino;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        contaOrigem = new ContaBancaria();
        contaOrigem.setId(UUID.randomUUID());
        contaOrigem.setSaldo(BigDecimal.valueOf(1000));
        contaOrigem.setCliente(new Cliente()); // Adiciona um cliente válido à conta origem

        contaDestino = new ContaBancaria();
        contaDestino.setId(UUID.randomUUID());
        contaDestino.setSaldo(BigDecimal.valueOf(500));
        contaDestino.setCliente(new Cliente()); // Adiciona um cliente válido à conta destino

        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void deveRealizarSaqueComSucesso() {
        BigDecimal valorSaque = BigDecimal.valueOf(200);

        when(contaBancariaService.buscarContaPorId(any(UUID.class))).thenReturn(contaOrigem);
        doNothing().when(contaBancariaService).realizarSaque(any(UUID.class), any(BigDecimal.class));

        Transacao transacao = transacaoService.realizarSaque(contaOrigem.getId(), valorSaque);

        assertEquals(TipoTransacao.SAQUE, transacao.getTipo());
        assertEquals(valorSaque, transacao.getValor());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficienteNoSaque() {
        BigDecimal valorSaque = BigDecimal.valueOf(1200);

        when(contaBancariaService.buscarContaPorId(any(UUID.class))).thenReturn(contaOrigem);

        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.realizarSaque(contaOrigem.getId(), valorSaque));
    }

    @Test
    public void deveRealizarDepositoComSucesso() {
        BigDecimal valorDeposito = BigDecimal.valueOf(300);

        when(contaBancariaService.buscarContaPorId(any(UUID.class))).thenReturn(contaDestino);
        doNothing().when(contaBancariaService).realizarDeposito(any(UUID.class), any(BigDecimal.class));

        Transacao transacao = transacaoService.realizarDeposito(contaDestino.getId(), valorDeposito);

        assertEquals(TipoTransacao.DEPOSITO, transacao.getTipo());
        assertEquals(valorDeposito, transacao.getValor());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    public void deveRealizarTransferenciaComSucesso() {
        BigDecimal valorTransferencia = BigDecimal.valueOf(400);

        when(contaBancariaService.buscarContaPorId(contaOrigem.getId())).thenReturn(contaOrigem);
        when(contaBancariaService.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);
        doNothing().when(contaBancariaService).realizarTransferencia(any(UUID.class), any(UUID.class),
                any(BigDecimal.class));

        Transacao transacao = transacaoService.realizarTransferencia(contaOrigem.getId(), contaDestino.getId(),
                valorTransferencia);

        assertEquals(TipoTransacao.TRANSFERENCIA, transacao.getTipo());
        assertEquals(valorTransferencia, transacao.getValor());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficienteNaTransferencia() {
        BigDecimal valorTransferencia = BigDecimal.valueOf(1500);

        when(contaBancariaService.buscarContaPorId(contaOrigem.getId())).thenReturn(contaOrigem);
        when(contaBancariaService.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);

        assertThrows(IllegalArgumentException.class, () -> transacaoService.realizarTransferencia(contaOrigem.getId(),
                contaDestino.getId(), valorTransferencia));
    }
}
