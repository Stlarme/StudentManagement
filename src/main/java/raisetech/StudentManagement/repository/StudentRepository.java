package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

 @Select("SELECT * FROM students WHERE isDeleted = false")
 List<Student> search();
;

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
 void insertStudentsCourse(StudentsCourses studentsCourse);


 @Select("SELECT * FROM students WHERE id = #{id}")
 Student findStudentById(String id);

 @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
 List<StudentsCourses> findCoursesByStudentId(String studentId);

 @Update("""
  UPDATE students SET 
    name = #{name}, kanaName = #{kanaName}, nickname = #{nickname}, email = #{email}, 
    region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark},
    isDeleted = #{isDeleted}
  WHERE id = #{id}
""")
 void updateStudent(Student student);


 @Update("""
    UPDATE students_courses SET
      course_name = #{courseName}, start_date = #{startDate}, end_date = #{endDate}
    WHERE id = #{id}
  """)
 void updateStudentsCourse(StudentsCourses studentsCourse);

}
