package infnet.pb.RSS.Bank.Auditoria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "infnet.pb.RSS.Bank.Auditoria.repository")
@EnableTransactionManagement
public class JpaConfig {
}
