package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual).isNotNull();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生のID指定検索が行えること() {
    Student student = sut.searchStudent("1");
    assertThat(student).isNotNull();
    assertThat(student.getId()).isEqualTo("1");
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentsCoursesList();
    assertThat(actual).isNotNull();
    assertThat(actual.size()).isGreaterThan(0);
  }

  @Test
  void 受講生IDに基づく受講生コース情報の検索が行えること() {
    List<StudentCourse> studentCourse = sut.searchStudentsCourses("1");
    assertThat(studentCourse).isNotNull();
    for (StudentCourse studentCourseList : studentCourse) {
      assertThat(studentCourseList.getStudentId()).isEqualTo("1");
      assertThat(studentCourse.size()).isEqualTo(3);
    }
  }

  @Test
  void 受講生の新規登録が行えること() {
    Student student = new Student(
        "", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "テスト県", 20, "男性", "備考", false
    );

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    assertThat(actual).isNotEmpty();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の新規登録が行えること() {
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = startDate.plusYears(1);

    StudentCourse studentCourse = new StudentCourse(
        null, "1", "Javaコース", startDate, endDate
    );

    sut.registerStudentsCourses(studentCourse);
    List<StudentCourse> studentCourseList = sut.searchStudentsCourses("1");
    assertThat(studentCourseList)
        .extracting("courseName")
        .contains("Javaコース");
  }


  @Test
  void 受講生の更新が行えること() {
    Student student = sut.searchStudent("1");
    student.setName("更新太郎");
    sut.updateStudent(student);

    Student updated = sut.searchStudent("1");
    assertThat(updated.getName()).isEqualTo("更新太郎");
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    List<StudentCourse> studentCourseList = sut.searchStudentsCourses("1");
    StudentCourse studentCourse = studentCourseList.get(0);
    studentCourse.setCourseName("更新コース");
    sut.updateStudentsCourse(studentCourse);

    List<StudentCourse> updatedCourses = sut.searchStudentsCourses("1");
    assertThat(updatedCourses.get(0).getCourseName()).isEqualTo("更新コース");
  }
}
