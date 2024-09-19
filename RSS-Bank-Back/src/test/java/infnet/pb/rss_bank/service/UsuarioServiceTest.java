package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import infnet.pb.rss_bank.RssBankApplication;
import infnet.pb.rss_bank.exception.UsuarioException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioServiceMock;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setSenha("Test@1234");
    }

    @Test
    void testAutenticacaoComSucesso() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioServiceMock.autenticarUsuario("testuser", "Test@1234");
        assertTrue(resultado);
    }

    @Test
    void testAutenticacaoSenhaIncorreta() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        assertThrows(UsuarioException.class, () -> usuarioServiceMock.autenticarUsuario("testuser", "senhaErrada"));
    }

    @Test
    void testRegistrarUsuarioComSucesso() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario novoUsuario = usuarioServiceMock.registrarUsuario(usuario);
        assertNotNull(novoUsuario);
    }

    @Test
    void testValidarNomeDeUsuarioDuplicado() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioServiceMock.registrarUsuario(usuario));
    }

    @Test
    void testSenhaFraca() {
        usuario.setSenha("12345");

        assertThrows(IllegalArgumentException.class, () -> usuarioServiceMock.registrarUsuario(usuario));
    }

    @Test
    void testDeletarUsuario() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        usuarioServiceMock.deletarUsuarioPorUsername("testuser");
        assertFalse(usuarioRepository.existsByUsername("testuser"));
    }
}
