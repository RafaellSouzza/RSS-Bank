package infnet.pb.RSS.Bank.Auditoria.service;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import infnet.pb.RSS.Bank.Auditoria.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuditoriaService {

   @Autowired
   private AuditoriaRepository auditoriaRepository;

   public List<Auditoria> getAllAuditorias() {
      return auditoriaRepository.findAll();
   }

   public Auditoria salvarAuditoria(Auditoria auditoria) {
      return auditoriaRepository.save(auditoria);
   }

   public Auditoria getAuditoriaById(UUID id) {
      Optional<Auditoria> auditoria = auditoriaRepository.findById(id);
      if (auditoria.isPresent()) {
         return auditoria.get();
      } else {
         throw new IllegalArgumentException("Auditoria não encontrada com o ID: " + id);
      }
   }

   public Auditoria saveAuditoria(Auditoria auditoria) {
      return auditoriaRepository.save(auditoria);
   }
}
