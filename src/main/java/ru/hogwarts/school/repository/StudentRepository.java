package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    Collection<Student> findByAgeBetween(int ageFrom, int ageTo);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    Integer getCountStudents();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    Double getAverageAgeStudents();

    @Query(value = "SELECT * FROM (SELECT * FROM student ORDER BY id DESC LIMIT :count) AS st ORDER BY st.id", nativeQuery = true)
    Collection<Student> getLastStudents(@Param("count") Integer count);
}
