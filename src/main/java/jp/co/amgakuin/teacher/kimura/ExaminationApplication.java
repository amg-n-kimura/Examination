package jp.co.amgakuin.teacher.kimura;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExaminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExaminationApplication.class, args);
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {//@Value("${secure.cookie}")boolean secure) {

        ServletContextInitializer servletContextInitializer = new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.getSessionCookieConfig().setHttpOnly(true);
                //servletContext.getSessionCookieConfig().setSecure(secure);
                servletContext.setSessionTrackingModes(
                        Collections.singleton(SessionTrackingMode.COOKIE));
            }
        };
        return servletContextInitializer;
    }
}
