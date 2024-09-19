package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.exception.UsuarioException;
import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setSenha("Test@1234");
    }

    @Test
    void testAutenticacaoComSucesso() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Test@1234", usuario.getSenha())).thenReturn(true);

        boolean resultado = usuarioService.autenticarUsuario("testuser", "Test@1234");
        assertTrue(resultado);
    }

    @Test
    void testAutenticacaoSenhaIncorreta() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", usuario.getSenha())).thenReturn(false);

        assertThrows(UsuarioException.class, () -> usuarioService.autenticarUsuario("testuser", "senhaErrada"));
    }

    @Test
    void testRegistrarUsuarioComSucesso() {
        when(passwordEncoder.encode("Test@1234")).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
        assertNotNull(novoUsuario);
        assertEquals("encodedPassword", novoUsuario.getSenha());
    }

    @Test
    void testValidarNomeDeUsuarioDuplicado() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(usuario));
    }

}
