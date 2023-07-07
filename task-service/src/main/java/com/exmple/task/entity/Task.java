package com.exmple.task.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String mail;
    private String text;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
}
