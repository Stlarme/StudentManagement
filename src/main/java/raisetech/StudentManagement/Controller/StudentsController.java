package raisetech.StudentManagement.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録などを行うREST APIとして実行されるController
 */
@Validated
@RestController
public class StudentsController {

  private StudentService service;

  @Autowired
  public StudentsController(StudentService service) {
    this.service = service;
  }

  /**
   * 　受講生一覧検索 全件検索をするので条件指定は行わない
   *
   * @return 受講生一覧（全件）
   */
  @Operation(
      summary = "受講生一覧取得",
      description = "全ての受講生情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "成功")
      }
  )
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
  @Operation(
      summary = "受講生取得",
      description = "指定されたIDの受講生情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "成功"),
          @ApiResponse(responseCode = "404", description = "該当データなし")
      }
  )
  @GetMapping("/Student/{id}")
  public StudentDetail getStudent(
      @Parameter(description = "受講生ID")
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行う
   *
   * @param studentDetail 　受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生登録",
      description = "受講生情報を新規登録します。",
      requestBody = @RequestBody(
          description = "登録する受講生情報",
          required = true,
          content = @Content(schema = @Schema(implementation = StudentDetail.class))
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "登録成功"),
          @ApiResponse(responseCode = "400", description = "バリデーションエラー")
      }
  )
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
  @Operation(
      summary = "受講生更新",
      description = "既存の受講生情報を更新します。",
      requestBody = @RequestBody(
          description = "更新する受講生情報",
          required = true,
          content = @Content(schema = @Schema(implementation = StudentDetail.class))
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "更新成功"),
          @ApiResponse(responseCode = "404", description = "対象データなし")
      }
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @Operation(
      summary = "例外テスト",
      description = "常に例外を発生させるテスト用APIです。",
      responses = {
          @ApiResponse(responseCode = "500", description = "強制例外発生")
      }
  )
  @GetMapping("/test")
  public String throwTestException() {
    throw new RuntimeException("例外が発生しました。");
  }
}
