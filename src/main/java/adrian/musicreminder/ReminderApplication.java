package adrian.musicreminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReminderApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReminderApplication.class, args);
	}
}