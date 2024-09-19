package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditoRepository extends JpaRepository<Credito, UUID> {
}
