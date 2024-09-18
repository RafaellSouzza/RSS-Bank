package infnet.pb.rss_bank.service;


import infnet.pb.rss_bank.exception.UsuarioException;
import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private static final int MAX_TENTATIVAS_LOGIN = 5;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        if (usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com nome: " + username);
        }

        Usuario usuario = usuarioOptional.get();
        return new User(usuario.getUsername(), usuario.getSenha(), Collections.emptyList()); // Sem roles por enquanto
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarDadosUsuario(usuario);

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Nome de usuário já está em uso.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public boolean autenticarUsuario(String username, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            if (usuario.getTentativasLogin() >= MAX_TENTATIVAS_LOGIN) {
                throw new UsuarioException("Conta bloqueada devido a várias tentativas de login.");
            }

            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                usuario.setTentativasLogin(0); // Zerar tentativas após login bem-sucedido
                usuarioRepository.save(usuario);
                return true;
            } else {
                int tentativas = usuario.getTentativasLogin() + 1;
                usuario.setTentativasLogin(tentativas);
                usuarioRepository.save(usuario);
                throw new UsuarioException("Senha incorreta. Tentativa " + tentativas + " de " + MAX_TENTATIVAS_LOGIN);
            }
        }
        return false;
    }

    public void deletarUsuario(UUID id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }

    public void deletarUsuarioPorUsername(String username) {
        if (usuarioRepository.existsByUsername(username)) {
            usuarioRepository.deleteByUsername(username);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }

    private void validarDadosUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário não pode ser vazio.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        if (!usuario.getSenha().matches(".*\\d.*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um número.");
        }
        if (!usuario.getSenha().matches(".*[!@#\\$%\\^&*].*")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos um caractere especial.");
        }
    }
}
