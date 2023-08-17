package com.exmple.task.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
public abstract class Domain {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private LocalDateTime createdTS;
    private String createdBy; // login of user that created object
    private LocalDateTime lastUpdatedTS;
    private String lastUpdatedBy; // login of user that update object
}
