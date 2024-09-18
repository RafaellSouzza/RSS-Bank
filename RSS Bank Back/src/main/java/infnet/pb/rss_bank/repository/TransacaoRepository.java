package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.StatusTransacao;
import infnet.pb.rss_bank.model.TipoTransacao;
import infnet.pb.rss_bank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaOrigemId(UUID contaOrigemId);

    List<Transacao> findByContaDestinoId(UUID contaDestinoId);

    List<Transacao> findByTipo(TipoTransacao tipo);

    List<Transacao> findByData(LocalDateTime data);

    List<Transacao> findByDataBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transacao> findByStatus(StatusTransacao status);

    List<Transacao> findByContaOrigemIdAndData(Long contaOrigemId, LocalDateTime data);

    List<Transacao> findByContaDestinoIdAndTipo(Long contaDestinoId, TipoTransacao tipo);

    List<Transacao> findByContaOrigemIdAndStatus(Long contaOrigemId, StatusTransacao status);
}
