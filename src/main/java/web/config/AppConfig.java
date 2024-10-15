package web.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableJpaRepositories("web.repository")
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@ComponentScan("web")
public class AppConfig {

    @Resource
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());
        em.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());

        return em;
    }

    private Properties getHibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

        return props;
    }

    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getRequiredProperty("db.url"));
        config.setDriverClassName(env.getRequiredProperty("db.driver"));
        config.setUsername(env.getRequiredProperty("db.username"));
        config.setPassword(env.getRequiredProperty("db.password"));
        config.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty("db.maxPoolSize")));
        config.setMinimumIdle(Integer.parseInt(env.getRequiredProperty("db.minIdle")));
        config.setConnectionTestQuery(env.getProperty("db.validationQuery"));
        config.setConnectionTimeout(30000);  // ¬рем€ ожидани€ подключени€ (в миллисекундах)
        config.setIdleTimeout(60000);  // ћаксимальное врем€ просто€ (в миллисекундах)
        config.setMaxLifetime(1800000);  // ћаксимальный срок жизни соединени€ (в миллисекундах)
        config.setValidationTimeout(5000);  // ¬рем€ ожидани€ проверки соединени€ (в миллисекундах)

        return new HikariDataSource(config);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @PreDestroy
    public void cleanup() {
        com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
    }
}
