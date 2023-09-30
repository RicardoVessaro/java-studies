package com.alibou.security.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
/*
 * User @Table(name="_user") to avoid ambiguity in the
 *  default tables created by databases. User table is
 *  created in PostgresSQl for example, maybe in MariaDB
 *  also. Our user table name is '_user'.
 */
@Table(name="_user")
public class User {

    @Id
    @GeneratedValue // Default values is GenerationType.AUTO.
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
