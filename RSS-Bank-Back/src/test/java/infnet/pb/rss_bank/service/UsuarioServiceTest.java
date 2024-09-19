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

class UsuarioServiceTest {

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

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    void testAutenticacaoComSucesso() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Test@1234", usuario.getSenha())).thenReturn(true);

        Usuario autenticado = usuarioService.autenticarUsuario("testuser", "Test@1234");
        assertNotNull(autenticado);
    }

    @Test
    void testAutenticacaoSenhaIncorreta() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", usuario.getSenha())).thenReturn(false);

        assertThrows(UsuarioException.class, () -> usuarioService.autenticarUsuario("testuser", "senhaErrada"));
    }

    @Test
    void testRegistrarUsuarioComSucesso() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
        assertNotNull(novoUsuario);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testValidarNomeDeUsuarioDuplicado() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(usuario));
    }

    @Test
    void testSenhaFraca() {
        usuario.setSenha("12345");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(usuario));
    }

    @Test
    void testDeletarUsuario() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuarioPorUsername("testuser");
        verify(usuarioRepository, times(1)).delete(usuario);
    }
}
