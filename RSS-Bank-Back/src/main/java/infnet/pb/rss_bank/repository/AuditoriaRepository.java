package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditoriaRepository extends JpaRepository<Auditoria, UUID> {
}
