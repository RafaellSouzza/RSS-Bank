package infnet.pb.rss_bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ContaBancaria {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String numeroConta;
    private BigDecimal saldo;
    private TipoConta tipoConta;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "contaBancaria", cascade = CascadeType.ALL)
    private List<Credito> creditos;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL)
    private List<Transacao> transacoesOrigem;

    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL)
    private List<Transacao> transacoesDestino;
}
