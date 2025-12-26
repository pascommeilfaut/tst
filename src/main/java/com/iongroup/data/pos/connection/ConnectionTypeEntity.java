package com.iongroup.data.pos.connection;

import com.iongroup.data.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "connection_types")
@Setter
@Getter
@NoArgsConstructor
public class ConnectionTypeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type ", unique = true, nullable = false, updatable = false)
    private ConnectionType value;
}
