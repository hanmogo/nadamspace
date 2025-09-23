package umc.nadamspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Jpa Auditing 활성화
@SpringBootApplication
public class NadamspaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NadamspaceApplication.class, args);
	}

}
