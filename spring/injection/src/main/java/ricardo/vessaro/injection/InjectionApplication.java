package ricardo.vessaro.injection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ricardo.vessaro.injection.service.Coach;
import ricardo.vessaro.injection.service.CoachFactory;
import ricardo.vessaro.injection.service.CoachType;

import java.util.List;

@SpringBootApplication
public class InjectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InjectionApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CoachFactory coachFactory) {
		return runner -> {
			Coach coach = coachFactory.create(CoachType.TENNIS);

			coach.training();
		};
	}

}
