package com.exmple.task.repository;

import com.exmple.task.entity.Task;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("UPDATE Task AS t SET t.mail=:mail, t.text=:text, t.title=:title, t.time=:time WHERE t.id=:id")
    void updateTask(@Param("id") long id, @Param("mail") String mail, @Param("text") String text, @Param("title") String title, @Param("time") Date time);

    List<Task> findAllByMail(String mail);

    @Query("FROM Task WHERE time>:startOfDay AND time<:endOfDay")
    List<Task> findAllByDate(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
}
