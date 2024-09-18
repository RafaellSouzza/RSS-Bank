package infnet.pb.RSS.Bank.Auditoria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Auditoria {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String descricao;

   private LocalDateTime dataEvento;

   private String tipoEvento;

   private String entidadeAfetada;

   private String ipOrigem;

   private String localizacao;

   private String usuarioResponsavel;

   public Auditoria() {
      this.dataEvento = LocalDateTime.now();
   }

   public Auditoria(String descricao, String tipoEvento, String entidadeAfetada, String ipOrigem, String localizacao,
         String usuarioResponsavel) {
      this.descricao = descricao;
      this.tipoEvento = tipoEvento;
      this.entidadeAfetada = entidadeAfetada;
      this.ipOrigem = ipOrigem;
      this.localizacao = localizacao;
      this.usuarioResponsavel = usuarioResponsavel;
      this.dataEvento = LocalDateTime.now();
   }

   public void setDescricaoEvento(String descricaoEvento) {
      this.descricao = descricaoEvento;
   }
}
