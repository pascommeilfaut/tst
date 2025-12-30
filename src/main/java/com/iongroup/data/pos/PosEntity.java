package com.iongroup.data.pos;

import com.iongroup.data.BaseEntity;
import com.iongroup.data.city.CityEntity;
import com.iongroup.data.pos.connection.ConnectionTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "pos")
@Setter
@Getter
@NoArgsConstructor
public class PosEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 50)
    private String telephone;

    @Column(length = 50)
    private String cellphone;

    @Column(nullable = false, length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_city", nullable = false)
    private CityEntity city;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false, length = 100)
    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conn_type")
    private ConnectionTypeEntity connectionType;

    private LocalTime morningOpening;

    private LocalTime morningClosing;

    private LocalTime afternoonOpening;

    private LocalTime afternoonClosing;

    @Column(length = 20)
    private String daysClosed;

    @Column(name = "insert_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select count(1) from issues i where i.id_pos = id)")
    private Integer issuesCount;

}
