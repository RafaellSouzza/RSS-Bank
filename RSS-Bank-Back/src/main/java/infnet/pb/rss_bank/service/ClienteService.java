package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Cliente;
import infnet.pb.rss_bank.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(UUID id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deletarCliente(UUID id) {
        clienteRepository.deleteById(id);
    }

    public Cliente atualizarCliente(UUID id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setCpf(clienteAtualizado.getCpf());
            cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }
}
