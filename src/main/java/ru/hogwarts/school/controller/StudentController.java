package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

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
        if ((ageFrom == null || ageFrom > 0) && (ageTo == null || ageTo > 0)) {
            return ResponseEntity.ok(studentService.findAll());
        }
        if ((ageFrom != null && ageFrom > 0) && (ageTo != null && ageTo > 0)) {
            return ResponseEntity.ok(studentService.findByAgeBetween(ageFrom, ageTo));
        }
        return ResponseEntity.ok(studentService.findByAge(ageFrom));
    }

    @GetMapping("student_faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@RequestParam Integer id) {
        if (id != null && id > 0) {
            return ResponseEntity.ok(studentService.findStudent(id).getFaculty());
        }
        return ResponseEntity.ok().build();
    }

}
