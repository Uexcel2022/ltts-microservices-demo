package com.uexcel.bus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
public class Route extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long routeId;
    private String region;
    private String origin;
    private String destination;
    private double price;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "bus_route",
            joinColumns = {@JoinColumn(name = "routeId",referencedColumnName = "routeId")},
            inverseJoinColumns = {@JoinColumn(name = "busCode",referencedColumnName = "busCode")}
    )
    private Set<Bus> buses = new HashSet<>();
}
