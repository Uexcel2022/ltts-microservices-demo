package com.uexcel.bus.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@JsonIgnoreProperties(value =
        {"createdBy","createdDate", "updatedBy","updatedDate"})
public class BaseEntity {
    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(insertable = false)
    @LastModifiedBy
    private String updatedBy;

    @Column(insertable = false)
    @LastModifiedDate
    private Date updatedDate;
}
