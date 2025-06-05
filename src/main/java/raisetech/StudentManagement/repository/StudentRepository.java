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
    INSERT INTO students(name, kana_name, nickname, email, region, age, gender, remark)
    VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark})
""")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);
}