package raisetech.StudentManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	@Autowired
	private StudentRepository repository;
	@Autowired
	private StudentCoursesRepository CoursesRepository;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/StudentList")
	public List<Student> getStudentList() {
		return repository.search();
	}

	@GetMapping("/StudentCourses")
	public List<StudentCourses> getStudentsCourses() {
		return CoursesRepository.search();
	}

}
