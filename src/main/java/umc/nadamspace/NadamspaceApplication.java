package umc.nadamspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing //Jpa Auditing 활성화
@EnableScheduling //특정 시간에 코드 자동 실행
@SpringBootApplication
public class NadamspaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NadamspaceApplication.class, args);
	}

}
