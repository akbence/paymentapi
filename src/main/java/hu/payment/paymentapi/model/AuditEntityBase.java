package hu.payment.paymentapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;


@Data
@MappedSuperclass
public abstract class AuditEntityBase {


    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;


    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;



    @PrePersist
    public void setCreatedOn() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedOn() {
        updatedOn = LocalDateTime.now();
    }
}
