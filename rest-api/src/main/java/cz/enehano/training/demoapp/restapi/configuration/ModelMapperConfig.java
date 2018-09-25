package cz.enehano.training.demoapp.restapi.configuration;

import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(String.class, LocalDateTime.class);
        modelMapper.createTypeMap(LocalDateTime.class, String.class);
        modelMapper.addConverter(toStringDateTime);
        modelMapper.addConverter(toDateTimeString);
        modelMapper.getTypeMap(String.class, LocalDateTime.class).setProvider(localDateTimeProvider);
        return modelMapper;
    }

    private Provider<LocalDateTime> localDateTimeProvider = new AbstractProvider<LocalDateTime>() {
        @Override
        public LocalDateTime get() {
            return LocalDateTime.now();
        }
    };

    private Converter<String, LocalDateTime> toStringDateTime = new AbstractConverter<String, LocalDateTime>() {
        @Override
        protected LocalDateTime convert(String source) {
            return (source == null) ? null : LocalDateTime.parse(source, formatter);
        }
    };

    private Converter<LocalDateTime, String> toDateTimeString = new AbstractConverter<LocalDateTime, String>() {
        @Override
        protected String convert(LocalDateTime source) {
            return (source == null) ? null : source.format(formatter);
        }
    };
}
