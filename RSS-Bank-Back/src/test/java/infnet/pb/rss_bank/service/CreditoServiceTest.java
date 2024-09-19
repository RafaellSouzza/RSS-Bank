package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Credito;
import infnet.pb.rss_bank.model.Parcela;
import infnet.pb.rss_bank.model.StatusParcela;
import infnet.pb.rss_bank.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditoServiceTest {

    @InjectMocks
    private CreditoService creditoService;

    @Mock
    private CreditoRepository creditoRepository;

    private Credito credito;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criar um objeto Credito para o teste
        credito = new Credito();
        credito.setDescricao("Crédito de Teste");
        credito.setValor(new BigDecimal("1000.00"));
        credito.setJuros(new BigDecimal("0.10"));  // 10% de juros
        credito.setParcelas(10); // 10 parcelas
        credito.setDataInicio(LocalDate.now());
    }

    @Test
    public void testCriarCredito() {
        when(creditoRepository.save(any(Credito.class))).thenReturn(credito);

        Credito creditoSalvo = creditoService.criarCredito(credito);

        BigDecimal valorEsperadoParcela = new BigDecimal("110.00").setScale(2, RoundingMode.HALF_UP);
        assertEquals(valorEsperadoParcela, creditoSalvo.getValorParcela().setScale(2, RoundingMode.HALF_UP));

        LocalDate dataFimEsperada = credito.getDataInicio().plusMonths(credito.getParcelas());
        assertEquals(dataFimEsperada, creditoSalvo.getDataFim());

        List<Parcela> parcelas = creditoSalvo.getParcelasList();
        assertEquals(10, parcelas.size());

        Parcela primeiraParcela = parcelas.get(0);
        assertEquals(1, primeiraParcela.getNumeroParcela());
        assertEquals(valorEsperadoParcela, primeiraParcela.getValorParcela().setScale(2, RoundingMode.HALF_UP));
        assertEquals(StatusParcela.PENDENTE, primeiraParcela.getStatus());
        assertEquals(credito.getDataInicio().plusMonths(1), primeiraParcela.getDataVencimento());

        Parcela ultimaParcela = parcelas.get(9);
        assertEquals(10, ultimaParcela.getNumeroParcela());
        assertEquals(valorEsperadoParcela, ultimaParcela.getValorParcela().setScale(2, RoundingMode.HALF_UP));
        assertEquals(StatusParcela.PENDENTE, ultimaParcela.getStatus());
        assertEquals(credito.getDataInicio().plusMonths(10), ultimaParcela.getDataVencimento());

        verify(creditoRepository, times(1)).save(credito);
    }

}
