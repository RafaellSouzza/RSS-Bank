package infnet.pb.rss_bank.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo; // Enum para garantir tipos válidos de transação

    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private ContaBancaria contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private ContaBancaria contaDestino;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status; // Status da transação (concluída, pendente, etc.)
}
