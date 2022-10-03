package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}") //GET http://localhost:8080/student/23
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping    //POST http://localhost:8080/student
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping //PUT http://localhost:8080/books
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")  //DELETE http://localhost:8080/student/23
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

 /*   @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }*/

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) Integer ageFrom, @RequestParam(required = false) Integer ageTo) {
        if ((ageFrom == null || ageFrom <= 0) && (ageTo == null || ageTo <= 0)) {
            return ResponseEntity.ok(studentService.findAll());
        }
        if ((ageFrom != null && ageFrom > 0) && (ageTo != null && ageTo > 0)) {
            return ResponseEntity.ok(studentService.findByAgeBetween(ageFrom, ageTo));
        }
        return ResponseEntity.ok(studentService.findByAge(ageFrom));
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Integer id) {
        if (id > 0) {
            return ResponseEntity.ok(studentService.findStudent(id).getFaculty());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCountStudents() {
        return ResponseEntity.ok(studentService.getCountStudents());
    }

    @GetMapping("/average_age")
    public ResponseEntity<Double> getAverageAgeStudents() {
        return ResponseEntity.ok(studentService.getAverageAgeStudents());
    }

    @GetMapping("/last_students")
    public ResponseEntity<Collection<Student>> getLastStudents(@RequestParam Integer count) {
        return ResponseEntity.ok(studentService.getLastStudents(count));
    }

    @GetMapping("/sorted_by_word")
    public ResponseEntity<List<String>> getStudentStartWithLetter(@RequestParam(name = "char") char letter) {
        return ResponseEntity.ok(studentService.getStudentStartWithLetter(letter));
    }

    @GetMapping("/average_age_stream")
    public ResponseEntity<Double> getAverageAgeStudentsWithStreams() {
        return ResponseEntity.ok(studentService.getAverageAgeStudentsWithStreams());
    }

    @GetMapping("/calc_parallel")
    public ResponseEntity<Integer> getCalcParallel() {
        return ResponseEntity.ok(studentService.getCalcParallel());
    }
}
