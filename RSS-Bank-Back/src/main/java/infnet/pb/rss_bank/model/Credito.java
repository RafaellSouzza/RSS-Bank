package infnet.pb.rss_bank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Credito {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String descricao;
    private BigDecimal valor;
    private BigDecimal juros;
    private int parcelas;
    private BigDecimal valorParcela;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    private StatusCredito status;

    @OneToMany(mappedBy = "credito", cascade = CascadeType.ALL)
    private List<Parcela> parcelasList;

    @ManyToOne
    @JoinColumn(name = "conta_bancaria_id", nullable = false)
    private ContaBancaria contaBancaria;

    public void calcularDataFim() {
        if (dataInicio != null && parcelas > 0) {
            this.dataFim = dataInicio.plusMonths(parcelas);
        }
    }
}
