package com.exmple.task.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tasks")
public class Task extends Domain {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String title;
    private String text;
    private LocalDateTime time;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(name = "user_mail")
    private String userMail;
}
