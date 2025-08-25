package com.backend.elearning.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class AppLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    private static final List<Locale> SUPPORTED = List.of(Locale.ENGLISH, new Locale("vi"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String header = request.getHeader("Accept-Language");
        if (header == null || header.isBlank()) return Locale.ENGLISH;
        Locale matched = Locale.lookup(Locale.LanguageRange.parse(header), SUPPORTED);
        return matched != null ? matched : Locale.ENGLISH;
    }

    @Bean
    public MessageSource messageSource() {
        var rs = new ResourceBundleMessageSource();
        rs.setBasenames("messages/messages"); // -> resources/messages/messages*.properties
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        rs.setFallbackToSystemLocale(false);
        return rs;
    }
}