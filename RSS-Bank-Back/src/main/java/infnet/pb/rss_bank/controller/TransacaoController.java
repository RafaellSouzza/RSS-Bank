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
    public ResponseEntity<Transacao> realizarSaque(
            @RequestParam String contaOrigemId,
            @RequestParam BigDecimal valor) {
        Transacao resultado = transacaoService.realizarSaque(UUID.fromString(contaOrigemId), valor);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/deposito")
    public ResponseEntity<Transacao> realizarDeposito(
            @RequestParam String contaDestinoId,
            @RequestParam BigDecimal valor) {
        Transacao resultado = transacaoService.realizarDeposito(UUID.fromString(contaDestinoId), valor);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Transacao> realizarTransferencia(
            @RequestParam String contaOrigemId,
            @RequestParam String contaDestinoId,
            @RequestParam BigDecimal valor) {
        Transacao resultado = transacaoService.realizarTransferencia(
                UUID.fromString(contaOrigemId),
                UUID.fromString(contaDestinoId),
                valor);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/pix")
    public ResponseEntity<Transacao> realizarPix(
            @RequestParam String contaOrigemId,
            @RequestParam String contaDestinoId,
            @RequestParam BigDecimal valor) {
        Transacao resultado = transacaoService.realizarPix(
                UUID.fromString(contaOrigemId),
                contaDestinoId,
                valor);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Transacao> realizarPagamento(
            @RequestParam String contaOrigemId,
            @RequestParam BigDecimal valor,
            @RequestParam String descricao) {
        Transacao resultado = transacaoService.realizarPagamento(
                UUID.fromString(contaOrigemId),
                valor,
                descricao);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/emprestimo")
    public ResponseEntity<Transacao> realizarEmprestimo(
            @RequestParam String contaDestinoId,
            @RequestParam BigDecimal valor) {
        Transacao resultado = transacaoService.realizarEmprestimo(
                UUID.fromString(contaDestinoId),
                valor);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/extrato/{contaId}")
    public ResponseEntity<List<Transacao>> obterExtrato(@PathVariable UUID contaId) {
        List<Transacao> transacoes = transacaoService.obterExtrato(contaId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        List<Transacao> transacoes = transacaoService.listarTransacoes();
        return ResponseEntity.ok(transacoes);
    }
}
