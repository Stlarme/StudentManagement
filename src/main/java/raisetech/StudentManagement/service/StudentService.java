package raisetech.StudentManagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses course : studentDetail.getStudentsCourses()) {
        course.setId("c-" + UUID.randomUUID().toString().substring(0, 8));
        course.setStudentId(studentDetail.getStudent().getId());
        repository.insertStudentsCourse(course);
      }
    }
  }

  public StudentDetail getStudentDetailById(String studentId) {
    Student student = repository.findStudentById(studentId);
    if (student == null) {
      return null;
    }
    List<StudentsCourses> courses = repository.findCoursesByStudentId(studentId);
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentsCourses(courses);
    return detail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses course : studentDetail.getStudentsCourses()) {
        repository.updateStudentsCourse(course);
      }
    }
  }

}
