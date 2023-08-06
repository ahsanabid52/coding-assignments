package de.element.travelguideservice

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("test")
@SpringBootTest(
    classes = [TravelGuideServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
@Sql(scripts = ["/sql-scripts/clear_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
abstract class AbstractIntegrationTest {
    companion object {
        private val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:12.3-alpine").apply {
            withDatabaseName("travel-guide-service")
            withUsername("user")
            withPassword("password")
            start()
        }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

            TestPropertyValues.of(
                "spring.datasource.url=${postgresqlContainer.jdbcUrl}",
                "spring.datasource.username=${postgresqlContainer.username}",
                "spring.datasource.password=${postgresqlContainer.password}",
                "spring.datasource.driver-class-name=${postgresqlContainer.driverClassName}",
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}