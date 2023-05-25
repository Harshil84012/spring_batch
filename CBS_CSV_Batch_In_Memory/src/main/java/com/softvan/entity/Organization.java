package com.softvan.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "organization_table")
@Data
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "name")
    private String name;
    @Column(name = "webSite")
    private String webSite;
    @Column(name = "country")
    private String country;
    @Column(name = "description")
    private String description;
    @Column(name = "founded")
    private String founded;
    @Column(name = "industry")
    private String industry;


}