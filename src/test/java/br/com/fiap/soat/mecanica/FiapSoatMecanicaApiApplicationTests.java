package br.com.fiap.soat.mecanica;

import br.com.fiap.soat.mecanica.support.PostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FiapSoatMecanicaApiApplicationTests extends PostgresIntegrationTest {

    @Test
    void contextLoads() {
    }
}
