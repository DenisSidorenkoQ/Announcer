package com.exmple.task.entity;

import javax.persistence.*;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends Domain {
    @Id
    private String mail;
    private String name;

    // TODO (vm): lets delete reference to tasks and try to handle exceptional situation
}
