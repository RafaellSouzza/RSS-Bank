package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, UUID> {

    List<ContaBancaria> findByClienteId(UUID clienteId);

    List<ContaBancaria> findByTipoConta(String tipoConta);

    ContaBancaria findByNumeroConta(String numeroConta);
}