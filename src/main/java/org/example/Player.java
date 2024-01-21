package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.sql.Persistable;

@Getter
@Setter
@AllArgsConstructor
public class Player implements Persistable {
    private Integer id;
    private String username;
    private String password;
    private Integer gamePoints;
}
