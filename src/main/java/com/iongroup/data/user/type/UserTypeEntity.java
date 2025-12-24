package com.iongroup.data.user.type;

import com.iongroup.data.BaseEntity;
import com.iongroup.data.user.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_types")
@Setter
@Getter
@NoArgsConstructor
public class UserTypeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", unique = true, nullable = false, updatable = false)
    private UserType value;
}
