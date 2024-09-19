package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.StatusTransacao;
import infnet.pb.rss_bank.model.TipoTransacao;
import infnet.pb.rss_bank.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    List<Transacao> findByContaOrigemId(UUID contaOrigemId);

    List<Transacao> findByContaDestinoId(UUID contaDestinoId);

    List<Transacao> findByTipo(TipoTransacao tipo);

    List<Transacao> findByData(LocalDateTime data);

    List<Transacao> findByDataBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transacao> findByStatus(StatusTransacao status);

    List<Transacao> findByContaOrigemIdAndData(UUID contaOrigemId, LocalDateTime data);

    List<Transacao> findByContaDestinoIdAndTipo(UUID contaDestinoId, TipoTransacao tipo);

    List<Transacao> findByContaOrigemIdAndStatus(UUID contaOrigemId, StatusTransacao status);

    List<Transacao> findByContaOrigemIdOrContaDestinoId(UUID contaOrigemId, UUID contaDestinoId);
}
