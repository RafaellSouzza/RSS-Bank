package infnet.pb.rss_bank.service;

import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("rafael123");
        usuario.setSenha("senhaSegura");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Usuario usuarioSalvo = usuarioService.registrarUsuario(usuario);

        assertNotNull(usuarioSalvo);
        assertEquals("rafael123", usuarioSalvo.getUsername());
        assertEquals("encodedPassword", usuarioSalvo.getSenha());
    }

    @Test
    public void testValidarNomeDeUsuarioDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setUsername("rafael123");
        usuario.setSenha("senhaSegura");

        when(usuarioRepository.existsByUsername("rafael123")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });
    }

    @Test
    public void testSenhaFraca() {
        Usuario usuario = new Usuario();
        usuario.setUsername("novoUsuario");
        usuario.setSenha("123");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });
    }

    @Test
    public void testRegistrarUsuarioComSenhaForte() {
        Usuario usuario = new Usuario();
        usuario.setUsername("forteUsuario");
        usuario.setSenha("Senha@Forte123");

        when(passwordEncoder.encode(anyString())).thenReturn("hashedSenhaForte");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.registrarUsuario(usuario);

        assertNotNull(usuarioSalvo);
        assertEquals("forteUsuario", usuarioSalvo.getUsername());
        assertEquals("hashedSenhaForte", usuarioSalvo.getSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void testAutenticacaoComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testeLogin");
        usuario.setSenha("Senha@Segura123");

        when(usuarioRepository.findByUsername("testeLogin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean autenticado = usuarioService.autenticarUsuario("testeLogin", "Senha@Segura123");

        assertTrue(autenticado);
    }


    @Test
    public void testAutenticacaoSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testeLoginIncorreto");
        usuario.setSenha("Senha@Segura123");

        when(usuarioRepository.findByUsername("testeLoginIncorreto")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.autenticarUsuario("testeLoginIncorreto", "SenhaErrada");
        });
    }


    @Test
    public void testDeletarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuarioParaDeletar");
        usuario.setSenha("Senha@123");

        when(usuarioRepository.existsById(any())).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(any());

        usuarioService.deletarUsuario(usuario.getId());

        verify(usuarioRepository, times(1)).deleteById(any());
    }
}
