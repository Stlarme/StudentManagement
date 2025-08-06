package raisetech.StudentManagement.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.Controller.converter.StudentsConverter;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録などを行うREST APIとして実行されるController
 */
@Validated
@RestController
@ControllerAdvice
public class StudentsController {

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
          setValue(null);
        } else {
          setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
      }

      @Override
      public String getAsText() {
        LocalDateTime value = (LocalDateTime) getValue();
        return (value != null) ? value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
            : "";
      }
    });
  }

  private StudentService service;
  private StudentsConverter converter;

  @Autowired
  public StudentsController(StudentService service, StudentsConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 　受講生一覧検索 全件検索をするので条件指定は行わない
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生検索 IDに紐づく任意の受講生情報を取得
   *
   * @param id 　受講生ID
   * @return 受講生情詳細
   */
  @GetMapping("/Student/{id}")
  public StudentDetail getStudent(
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行う
   *
   * @param studentDetail 　受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新 キャンセルフラグの更新もここで行う（論理削除）
   *
   * @param studentDetail 　受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @GetMapping("/test")
  public String throwTestException() {
    throw new RuntimeException("例外が発生しました。");
  }
}
