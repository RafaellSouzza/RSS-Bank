package infnet.pb.rss_bank.config;

import infnet.pb.rss_bank.model.Cliente;
import infnet.pb.rss_bank.model.ContaBancaria;
import infnet.pb.rss_bank.model.TipoConta;
import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.repository.ClienteRepository;
import infnet.pb.rss_bank.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class MockDataConfig {

    private final PasswordEncoder passwordEncoder;

    public MockDataConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        return args -> {
            populateData(usuarioRepository, clienteRepository);
        };
    }

    @Transactional
    public void populateData(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Rafa Souza");
        cliente1.setCpf("123.456.789-01");
        cliente1.setDataNascimento("1990-01-01");

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Oliveira");
        cliente2.setCpf("987.654.321-02");
        cliente2.setDataNascimento("1985-02-15");

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Santos");
        cliente3.setCpf("321.654.987-03");
        cliente3.setDataNascimento("1995-03-25");

        Cliente cliente4 = new Cliente();
        cliente4.setNome("Ana Costa");
        cliente4.setCpf("654.321.987-04");
        cliente4.setDataNascimento("1980-12-30");

        ContaBancaria conta1 = new ContaBancaria();
        conta1.setNumeroConta("0001");
        conta1.setSaldo(new BigDecimal("1000.00"));
        conta1.setTipoConta(TipoConta.CORRENTE);
        conta1.setCliente(cliente1);

        ContaBancaria conta2 = new ContaBancaria();
        conta2.setNumeroConta("0002");
        conta2.setSaldo(new BigDecimal("2000.00"));
        conta2.setTipoConta(TipoConta.POUPANCA);
        conta2.setCliente(cliente2);

        ContaBancaria conta3 = new ContaBancaria();
        conta3.setNumeroConta("0003");
        conta3.setSaldo(new BigDecimal("3000.00"));
        conta3.setTipoConta(TipoConta.CORRENTE);
        conta3.setCliente(cliente3);

        ContaBancaria conta4 = new ContaBancaria();
        conta4.setNumeroConta("0004");
        conta4.setSaldo(new BigDecimal("4000.00"));
        conta4.setTipoConta(TipoConta.POUPANCA);
        conta4.setCliente(cliente4);

        cliente1.setContas(Arrays.asList(conta1));
        cliente2.setContas(Arrays.asList(conta2));
        cliente3.setContas(Arrays.asList(conta3));
        cliente4.setContas(Arrays.asList(conta4));

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
        clienteRepository.save(cliente3);
        clienteRepository.save(cliente4);

        Usuario usuario1 = new Usuario();
        usuario1.setUsername("Rafa");
        usuario1.setSenha(passwordEncoder.encode("123"));
        usuario1.setCliente(cliente1);

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("maria");
        usuario2.setSenha(passwordEncoder.encode("password456"));
        usuario2.setCliente(cliente2);

        Usuario usuario3 = new Usuario();
        usuario3.setUsername("pedro");
        usuario3.setSenha(passwordEncoder.encode("password789"));
        usuario3.setCliente(cliente3);

        Usuario usuario4 = new Usuario();
        usuario4.setUsername("ana");
        usuario4.setSenha(passwordEncoder.encode("password101"));
        usuario4.setCliente(cliente4);

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
        usuarioRepository.save(usuario3);
        usuarioRepository.save(usuario4);
    }
}
