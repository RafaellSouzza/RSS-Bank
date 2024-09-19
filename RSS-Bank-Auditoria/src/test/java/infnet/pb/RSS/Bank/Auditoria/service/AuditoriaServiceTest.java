package infnet.pb.RSS.Bank.Auditoria.service;

import infnet.pb.RSS.Bank.Auditoria.model.Auditoria;
import infnet.pb.RSS.Bank.Auditoria.repository.AuditoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditoriaServiceTest {

    @InjectMocks
    private AuditoriaService auditoriaService;

    @Mock
    private AuditoriaRepository auditoriaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuditorias() {
        Auditoria auditoria1 = new Auditoria("Evento 1", "TIPO1", "Entidade1", "127.0.0.1", "Local1", "User1");
        Auditoria auditoria2 = new Auditoria("Evento 2", "TIPO2", "Entidade2", "127.0.0.2", "Local2", "User2");
        List<Auditoria> auditorias = Arrays.asList(auditoria1, auditoria2);

        when(auditoriaRepository.findAll()).thenReturn(auditorias);

        List<Auditoria> result = auditoriaService.getAllAuditorias();
        assertEquals(2, result.size());
        assertEquals(auditoria1.getDescricao(), result.get(0).getDescricao());
        assertEquals(auditoria2.getDescricao(), result.get(1).getDescricao());

        verify(auditoriaRepository, times(1)).findAll();
    }

    @Test
    void testSalvarAuditoria() {
        Auditoria auditoria = new Auditoria("Evento Teste", "TIPO_TESTE", "EntidadeTeste", "127.0.0.1", "LocalTest", "UserTest");
        when(auditoriaRepository.save(auditoria)).thenReturn(auditoria);

        Auditoria result = auditoriaService.salvarAuditoria(auditoria);
        assertNotNull(result);
        assertEquals("Evento Teste", result.getDescricao());

        verify(auditoriaRepository, times(1)).save(auditoria);
    }

    @Test
    void testGetAuditoriaById_Success() {
        Auditoria auditoria = new Auditoria("Evento Teste", "TIPO_TESTE", "EntidadeTeste", "127.0.0.1", "LocalTest", "UserTest");
        UUID auditoriaId = UUID.randomUUID();
        auditoria.setId(auditoriaId);
        when(auditoriaRepository.findById(auditoriaId)).thenReturn(Optional.of(auditoria));

        Auditoria result = auditoriaService.getAuditoriaById(auditoriaId);
        assertNotNull(result);
        assertEquals(auditoriaId, result.getId());
        assertEquals("Evento Teste", result.getDescricao());

        verify(auditoriaRepository, times(1)).findById(auditoriaId);
    }

    @Test
    void testGetAuditoriaById_NotFound() {
        UUID auditoriaId = UUID.randomUUID();
        when(auditoriaRepository.findById(auditoriaId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            auditoriaService.getAuditoriaById(auditoriaId);
        });

        String expectedMessage = "Auditoria não encontrada com o ID: " + auditoriaId;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(auditoriaRepository, times(1)).findById(auditoriaId);
    }
}
