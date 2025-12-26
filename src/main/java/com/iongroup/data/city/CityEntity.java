package com.iongroup.data.city;

import com.iongroup.data.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Setter
@Getter
@NoArgsConstructor
public class CityEntity extends BaseEntity {

    @Column(name = "city_name", nullable = false, length = 50)
    private String name;
}
