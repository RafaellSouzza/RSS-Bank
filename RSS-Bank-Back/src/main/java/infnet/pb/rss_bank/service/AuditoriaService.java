package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Auditoria;
import infnet.pb.rss_bank.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public void registrarAuditoria(String acao, String tipoTransacao, UUID contaId, BigDecimal valor, String usuarioResponsavel) {
        Auditoria auditoria = new Auditoria();
        auditoria.setAcao(acao);
        auditoria.setTipoTransacao(tipoTransacao);
        auditoria.setContaId(contaId);
        auditoria.setValor(valor);
        auditoria.setData(LocalDateTime.now());
        auditoria.setUsuarioResponsavel(usuarioResponsavel);

        auditoriaRepository.save(auditoria);
    }
}
