package com.exmple.task.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.*;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String mail;
    private String title;
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
}
