package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Cliente;
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

    @Mock
    private AuditoriaService auditoriaService; // Mock do AuditoriaService

    @InjectMocks
    private TransacaoService transacaoService;

    private ContaBancaria contaOrigem;
    private ContaBancaria contaDestino;
    private Cliente clienteOrigem;
    private Cliente clienteDestino;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        clienteOrigem = new Cliente();
        clienteOrigem.setNome("Cliente Origem");

        clienteDestino = new Cliente();
        clienteDestino.setNome("Cliente Destino");

        contaOrigem = new ContaBancaria();
        contaOrigem.setId(UUID.randomUUID());
        contaOrigem.setSaldo(BigDecimal.valueOf(1000));
        contaOrigem.setCliente(clienteOrigem);

        contaDestino = new ContaBancaria();
        contaDestino.setId(UUID.randomUUID());
        contaDestino.setSaldo(BigDecimal.valueOf(500));
        contaDestino.setCliente(clienteDestino);

        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }



    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficienteNoSaque() {
        BigDecimal valorSaque = BigDecimal.valueOf(1200);

        when(contaBancariaService.buscarContaPorId(any(UUID.class))).thenReturn(contaOrigem);

        assertThrows(IllegalArgumentException.class,
                () -> transacaoService.realizarSaque(contaOrigem.getId(), valorSaque));
    }


    @Test
    public void deveRealizarTransferenciaComSucesso() {
        BigDecimal valorTransferencia = BigDecimal.valueOf(400);

        when(contaBancariaService.buscarContaPorId(contaOrigem.getId())).thenReturn(contaOrigem);
        when(contaBancariaService.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);
        doNothing().when(contaBancariaService).realizarTransferencia(any(UUID.class), any(UUID.class),
                any(BigDecimal.class));
        doNothing().when(auditoriaService).registrarAuditoria(anyString(), anyString(), any(UUID.class), any(BigDecimal.class), anyString()); // Mock do AuditoriaService

        Transacao transacao = transacaoService.realizarTransferencia(contaOrigem.getId(), contaDestino.getId(),
                valorTransferencia);

        assertEquals(TipoTransacao.TRANSFERENCIA, transacao.getTipo());
        assertEquals(valorTransferencia, transacao.getValor());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
        verify(auditoriaService, times(1)).registrarAuditoria(anyString(), anyString(), any(UUID.class), any(BigDecimal.class), anyString());
    }

}
