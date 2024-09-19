package infnet.pb.RSS.Bank.Auditoria.repository;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, UUID> {
   List<Auditoria> findAllByDataEventoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
}
