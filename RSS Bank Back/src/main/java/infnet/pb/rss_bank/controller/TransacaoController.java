package infnet.pb.rss_bank.controller;

import infnet.pb.rss_bank.model.Transacao;
import infnet.pb.rss_bank.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/saque")
    public ResponseEntity<Transacao> realizarSaque(@RequestParam UUID contaId, @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.realizarSaque(contaId, valor);
        return ResponseEntity.ok(transacao);
    }

    @PostMapping("/deposito")
    public ResponseEntity<Transacao> realizarDeposito(@RequestParam UUID contaId, @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.realizarDeposito(contaId, valor);
        return ResponseEntity.ok(transacao);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Transacao> realizarTransferencia(@RequestParam UUID contaOrigemId, @RequestParam UUID contaDestinoId, @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.realizarTransferencia(contaOrigemId, contaDestinoId, valor);
        return ResponseEntity.ok(transacao);
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        List<Transacao> transacoes = transacaoService.listarTransacoes();
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/origem/{contaOrigemId}")
    public ResponseEntity<List<Transacao>> buscarTransacoesPorContaOrigem(@PathVariable UUID contaOrigemId) {
        List<Transacao> transacoes = transacaoService.buscarTransacoesPorContaOrigem(contaOrigemId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/destino/{contaDestinoId}")
    public ResponseEntity<List<Transacao>> buscarTransacoesPorContaDestino(@PathVariable UUID contaDestinoId) {
        List<Transacao> transacoes = transacaoService.buscarTransacoesPorContaDestino(contaDestinoId);
        return ResponseEntity.ok(transacoes);
    }
}
