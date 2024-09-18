package infnet.pb.rss_bank.controller;

import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaBancariaController {

    @Autowired
    private ContaBancariaService contaBancariaService;

    @GetMapping("/cliente/{clienteId}")
    public List<ContaBancaria> getContasPorCliente(@PathVariable UUID clienteId) {
        return contaBancariaService.buscarContasPorCliente(clienteId);
    }

    @GetMapping("/numero/{numeroConta}")
    public ContaBancaria getContaPorNumero(@PathVariable String numeroConta) {
        return contaBancariaService.buscarContaPorNumero(numeroConta);
    }

    @GetMapping("/tipo/{tipoConta}")
    public List<ContaBancaria> getContasPorTipo(@PathVariable String tipoConta) {
        return contaBancariaService.buscarContasPorTipo(tipoConta);
    }
}