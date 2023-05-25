package com.softvan.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "student_tbl")
public class Student {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "birth_date")
    private String birthdate;

}
