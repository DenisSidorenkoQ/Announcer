package com.exmple.task.repository;

import com.exmple.task.entity.Task;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //  TODO: точно ли для update нужно добавлять Query? Вообще такой общий метод обновления очень опасен. Предлагаю подумать на что его лучше заменить.
    //  можно делать через save но тогда мы будем обновлять все объект, а нам нужны только текст, заголовок и время таски
    @Modifying
    @Query("UPDATE Task AS t SET t.text=:text, t.title=:title, t.time=:time WHERE t.id=:id")
    void updateTask(@Param("id") long id, @Param("text") String text, @Param("title") String title, @Param("time") Date time);

    List<Task> findTasksByUserMail(String mail);

    @Query("FROM Task  " +
            "WHERE time<:lastDate AND id>:lastId AND ( status='ACTIVE' OR status='RETRY' ) " +
            "ORDER BY time, id")
    List<Task> findActiveTasksByDateAndId(@Param("lastDate") LocalDateTime lastDate,
                                          @Param("lastId") long lastId,
                                          Pageable pageable);

}
