package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Credito;
import infnet.pb.rss_bank.model.Parcela;
import infnet.pb.rss_bank.model.StatusParcela;
import infnet.pb.rss_bank.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Transactional
    public Credito criarCredito(Credito credito) {
        BigDecimal valorTotal = credito.getValor();
        BigDecimal juros = credito.getJuros();
        int numParcelas = credito.getParcelas();

        BigDecimal valorParcela = calcularValorParcela(valorTotal, juros, numParcelas);
        credito.setValorParcela(valorParcela);

        credito.calcularDataFim();

        List<Parcela> parcelasList = new ArrayList<>();
        for (int i = 1; i <= numParcelas; i++) {
            Parcela parcela = new Parcela();
            parcela.setNumeroParcela(i);
            parcela.setValorParcela(valorParcela);
            parcela.setDataVencimento(credito.getDataInicio().plusMonths(i));
            parcela.setStatus(StatusParcela.PENDENTE);
            parcela.setCredito(credito);
            parcelasList.add(parcela);
        }
        credito.setParcelasList(parcelasList);

        return creditoRepository.save(credito);
    }

    private BigDecimal calcularValorParcela(BigDecimal valorTotal, BigDecimal juros, int numParcelas) {
        BigDecimal valorComJuros = valorTotal.add(valorTotal.multiply(juros));
        return valorComJuros.divide(BigDecimal.valueOf(numParcelas), BigDecimal.ROUND_HALF_UP);
    }
}
