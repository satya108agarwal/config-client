package config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CustomBootstrapRegistryInitializer implements BootstrapRegistryInitializer {

    private static Log logger = LogFactory.getLog(CustomBootstrapRegistryInitializer.class);

    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("In CustomBootstrapRegistryInitializer");
        registry.register(RestTemplate.class, context -> {
            RestTemplate restTemplate = customRestTemplate();
            // Customize RestTemplate here
            return restTemplate;


        });
    }



   // @Value("${spring.cloud.config.auth.username}")
    private String jwtUsername="Drimen, Orys";

    //@Value("${spring.cloud.config.auth.password:password1}")
    private String jwtPassword="password1";

   // @Value("${spring.cloud.config.auth.endpoint}")
    private String jwtEndpoint="http://localhost:8090/auth/login";

   // @Value("${spring.cloud.config.auth.readtimeout}")
    private int readtimeout=100000;

    private String jwtToken;

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void init() {
        RestTemplate restTemplate = new RestTemplate();

        models.LoginRequest loginBackend = new models.LoginRequest();
        loginBackend.setUserId(jwtUsername);
        loginBackend.setPassword(jwtPassword);

        String url = jwtEndpoint;
        models.Token token;
        try {
            token = restTemplate.postForObject(url, loginBackend, models.Token.class);
            System.out.println("*********");
            System.out.println(token);
            if (token.getToken() == null) {
                throw new Exception();
            }

            setJwtToken(token.getToken());
        } catch (Exception e) {
            logger.error("Can not fetch JWT from Config Server");
        }
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }




    private RestTemplate customRestTemplate() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwtToken);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(readtimeout);
        RestTemplate template = new RestTemplate(requestFactory);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer " + jwtToken));
        System.out.println("restemplate satya");
        template.setInterceptors(interceptors);




        return template;
    }

    public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String headerName;

        private final String headerValue;

        public HeaderRequestInterceptor(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().set(headerName, headerValue);
            System.out.println("In Interceptor");
            return execution.execute(request, body);
        }
    }
}