package web.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Метод, указывающий на классы корневой конфигурации (например, настройки базы данных и JPA)
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                AppConfig.class // Здесь может быть конфигурация JPA, базы данных и других бинов
        };
    }

    // Добавление конфигурации, в которой инициализируем ViewResolver, для корректного отображения страниц
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class // Конфигурация Spring MVC и Thymeleaf
        };
    }

    // Метод указывает URL, на котором будет базироваться приложение
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"}; // Все запросы будут обрабатываться через DispatcherServlet
    }

    // Метод для дополнительной конфигурации при старте сервлета
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        registerHiddenFieldFilter(servletContext); // Регистрация фильтра для поддержки методов PUT и DELETE
    }

    // Регистрация фильтра для поддержки скрытых HTTP-методов
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }

    // Дополнительно можно зарегистрировать и настроить DispatcherServlet вручную, если необходимо
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
    }
}