import config.CustomBootstrapRegistryInitializer;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;

@SpringBootApplication
public class CloudconfigclientApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CloudconfigclientApplication.class);
		application.run(args);
	}

}
