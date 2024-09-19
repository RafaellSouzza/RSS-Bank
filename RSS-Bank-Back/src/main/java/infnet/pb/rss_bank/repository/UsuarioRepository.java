package infnet.pb.rss_bank.repository;

import infnet.pb.rss_bank.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);

    void deleteByUsername(String username);
}
