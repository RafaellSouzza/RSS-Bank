package infnet.pb.RSS.Bank.Auditoria.listener;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import infnet.pb.RSS.Bank.Auditoria.service.AuditoriaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransacaoListener {

   @Autowired
   private AuditoriaService auditoriaService;

   @RabbitListener(queues = "${rabbitmq.queue.transacao}")
   public void processarMensagemDeTransacao(String mensagem) {
      Auditoria auditoria = new Auditoria();
      auditoria.setDescricaoEvento("Transação recebida: " + mensagem);

      auditoriaService.salvarAuditoria(auditoria);
   }
}
