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

	private String name = "Enami Kouji";
	private String age = "37";

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/Student")
	public String getStudent(@RequestParam String name) {
		Student student = repository.searchByName(name);
		return student.getName() + student.getAge() + "歳";
	}

	@PostMapping("/Student")
	public void registerStudent(String name, int age) {
		repository.registerStudent(name, age);
	}

	@PatchMapping("/Student")
	public void updateStudent(String name, int age) {
		repository.updateStudent(name, age);
	}

	@DeleteMapping("/Student")
	public void deleteStudent(String name) {
		repository.deleteStudent(name);
	}

	@GetMapping("/StudentList")
	public List<Student> getAllStudents() {
		return repository.findAll();
	}

}
