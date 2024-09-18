package infnet.pb.RSS.Bank.Auditoria.controller;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import infnet.pb.RSS.Bank.Auditoria.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

   private AuditoriaService auditoriaService;

   @Autowired
   public AuditoriaController(AuditoriaService auditoriaService) {
      this.auditoriaService = auditoriaService;
   }

   @GetMapping
   public List<Auditoria> getAllAuditorias() {
      return auditoriaService.getAllAuditorias();
   }

   @GetMapping("/{id}")
   public Auditoria getAuditoriaById(@PathVariable Long id) {
      return auditoriaService.getAuditoriaById(id);
   }

   @PostMapping
   public Auditoria saveAuditoria(@RequestBody Auditoria auditoria) {
      return auditoriaService.saveAuditoria(auditoria);
   }
}
