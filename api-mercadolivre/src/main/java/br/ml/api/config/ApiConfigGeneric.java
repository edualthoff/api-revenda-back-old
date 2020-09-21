package br.ml.api.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.ml.api.config.elk.TenantInterceptorService;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiConfigGeneric implements WebMvcConfigurer {
	
	@Autowired
	private TenantInterceptorService tenantInterceptorService;
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasename("messages/ApiErrorMessages");
		// ms.setDefaultEncoding("UTF-8");
		ms.setUseCodeAsDefaultMessage(true);

		return ms;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tenantInterceptorService);
	}
	
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";

    
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
    		DateFormat dateFormat2 = new SimpleDateFormat(dateTimeFormat);
    		//dateFormat2.setTimeZone(TimeZone.getDefault());
        	builder.modulesToInstall(new JavaTimeModule());
        	builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        	builder.featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        	builder.featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
            builder.dateFormat(dateFormat2);
            builder.findModulesViaServiceLoader(true);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }
	
    /**
         @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        System.out.println("Config is starting.");
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
      @Bean
	public Jackson2ObjectMapperBuilder jacksonObjectMapperCustomization() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
		
		Jackson2ObjectMapperBuilder build = new Jackson2ObjectMapperBuilder();
		build.indentOutput(true);
		build.createXmlMapper(false);
		//build.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		//build.featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
		//build.featuresToDisable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
		//build.featuresToDisable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
		//build.dateFormat(dateFormat);
		return build;
	}

	@PostConstruct
	public void dateZone() {
	    //TimeZone.setDefault(TimeZone.getTimeZone("GMT-0300"));
		LocalDateTime oi = LocalDateTime.now();
		
		System.out.println("date: "+oi+" ");
		try {
			System.out.println("dd: " +new ObjectMapper().writeValueAsString(oi));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" -- -- --");
	}*/
}
