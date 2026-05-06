package ma.scs.inventory_app;


import ma.scs.inventory_app.repository.jpa.AreaRepository;
import ma.scs.inventory_app.repository.jpa.UserRepository;
import ma.scs.inventory_app.service.iml.InventoryRangeServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


//@EnableWebFlux
//

@SpringBootApplication 
//@EnableJpaRepositories(basePackages = "ma.scs.inventory_app.repository.jpa")
public class InventoryAppApplication {

	public static void main(String[] args) {
		//System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");
		SpringApplication.run(InventoryAppApplication.class, args);
		
		
	}
	/*@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			if (userRepository.findById("admin").isEmpty()) {
				User admin = new User();
				admin.setFullName("Admin");
				admin.setUserId("00");
				admin.setPassword("admin123"); // (Later: encode)
				admin.setRole("ADMIN");
				userRepository.save(admin);
			}
		};
	}*/




	@Bean
	CommandLineRunner migrateData(
			// OldAreaRepository oldAreaRepo,
			//OldInventoryTeamRepository oldTeamRepo,
			AreaRepository newAreaRepo,
			UserRepository newUserRepo
	) {
		return args -> {
			// Step 1: Migrate Area table
			/*Map<Long, Area> areaMap = new HashMap<>();
			for (OldArea oldArea : oldAreaRepo.findAll()) {
			Area newArea = new Area();
			newArea.setArea(oldArea.getArea());
			newArea.setAreaId(oldArea.getAreaNo());
			newArea.setAreaDesc(oldArea.getAreaDesc());
			newArea.setStartPlage(oldArea.getStart_p());
			newArea.setEndPlage(oldArea.getEnd_p());
			newArea.setOldAreaNo(oldArea.getAreaNo());

			newArea = newAreaRepo.save(newArea);

			areaMap.put(oldArea.getAreaNo(), newArea); // Key should match oldArea.getAreaNo()
		}


			// Step 2: Migrate InventoryTeam to Users
			for (OldInventoryTeam oldUser : oldTeamRepo.findAll()) {
				User user = new User();
				user.setUserId(oldUser.getUserID());
				user.setFullName(oldUser.getFullName());
				user.setPassword(oldUser.getPassword());
				user.setIsLoggedIn(oldUser.getIsLoggedIn());
				user.setRole("USER");

				// Foreign key handling
				Area linkedArea = areaMap.get(oldUser.getAreaNo());
				if (linkedArea != null) {
					user.setArea(linkedArea);
				}

				newUserRepo.save(user);
			}

			System.out.println("✅ Data migration completed successfully.");*/
		};
	}

}
