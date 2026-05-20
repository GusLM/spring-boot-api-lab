package com.gustavosantos.library_api.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    // Injeta a URL de conexão com o banco de dados a partir da variável de ambiente DATABASE_URL.
    @Value("${DATABASE_URL}")
    String databaseUrl;

    // Injeta o usuário utilizado para autenticação no banco de dados.
    @Value("${DATABASE_USERNAME}")
    String databaseUsername;

    // Injeta a senha utilizada para autenticação no banco de dados.
    @Value("${DATABASE_PASSWORD}")
    String databasePassword;

    // Injeta o nome da classe do driver JDBC utilizado pela aplicação.
    @Value("${DATABASE_DRIVER_CLASS_NAME}")
    String databaseDriverClassName;

    /*
     * Configuração do HikariCP, implementação de pool de conexões usada pela aplicação.
     * Define as propriedades necessárias para estabelecer conexões com o banco de dados.
     *
     */

    @Bean
    public DataSource hikariDataSource(){
        // Cria a configuração do HikariCP, implementação de pool de conexões usada pela aplicação.
        HikariConfig config = new HikariConfig();

        // Define os dados básicos necessários para abrir conexões com o banco.
        config.setJdbcUrl(databaseUrl);
        config.setUsername(databaseUsername);
        config.setPassword(databasePassword);
        config.setDriverClassName(databaseDriverClassName);

        // Define o número máximo de conexões que podem existir simultaneamente no pool.
        config.setMaximumPoolSize(10);

        // Mantém pelo menos uma conexão ociosa disponível para reduzir o tempo de espera.
        config.setMinimumIdle(1);

        // Define um nome para o pool, facilitando a identificação em logs e monitoramento.
        config.setPoolName("library-db-pool");

        // Define o tempo máximo de vida de uma conexão no pool, em milissegundos.
        config.setMaxLifetime(600000);

        // Define o tempo máximo de espera para obter uma conexão do pool, em milissegundos.
        config.setConnectionTimeout(30000);

        // Consulta simples usada para validar se a conexão com o banco está ativa.
        config.setConnectionTestQuery("SELECT 1");

        // Retorna o DataSource configurado, que será gerenciado pelo Spring como um bean.
        return new HikariDataSource(config);
    }
}
