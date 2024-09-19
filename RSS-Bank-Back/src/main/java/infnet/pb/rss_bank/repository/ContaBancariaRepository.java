package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, UUID> {

    List<ContaBancaria> findByClienteId(UUID clienteId);

    List<ContaBancaria> findByTipoConta(TipoConta tipoConta);

    ContaBancaria findByNumeroConta(String numeroConta);
}
