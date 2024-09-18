package infnet.pb.RSS.Bank.Auditoria.repository;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
   List<Auditoria> findAllByDataEventoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
}
