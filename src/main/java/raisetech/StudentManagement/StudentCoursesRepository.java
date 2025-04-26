package raisetech.StudentManagement;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StudentCoursesRepository {

  @Select("SELECT * FROM students_courses")
  List<StudentCourses> search();
}