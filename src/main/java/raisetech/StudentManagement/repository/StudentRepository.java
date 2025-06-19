package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

 @Select("SELECT * FROM students")
 List<Student> search();

 @Select("SELECT * FROM students_courses")
 List<StudentsCourses> searchStudentsCourses();

 @Insert("""
         INSERT INTO students(name, kanaName, nickname, email, region, age, gender, remark)
         VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark})
     """)
 @Options(useGeneratedKeys = true, keyProperty = "id")
 void insertStudent(Student student);

 @Insert("""
         INSERT INTO students_courses(id, student_id, course_name, start_date, end_date)
         VALUES(#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDate})
     """)
 @Options(useGeneratedKeys = false)
  // 自動採番ではないのでfalse
 void insertStudentsCourse(StudentsCourses studentsCourse);
}
