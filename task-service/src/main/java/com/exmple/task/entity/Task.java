package com.exmple.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
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
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String title;
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User author;
}
