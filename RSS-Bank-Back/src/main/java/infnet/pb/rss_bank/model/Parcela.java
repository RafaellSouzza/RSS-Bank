package infnet.pb.rss_bank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Parcela {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private int numeroParcela;
    private BigDecimal valorParcela;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusParcela status;

    @ManyToOne
    @JoinColumn(name = "credito_id")
    private Credito credito;
}
