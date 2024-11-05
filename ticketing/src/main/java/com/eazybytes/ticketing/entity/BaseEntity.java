package com.eazybytes.ticketing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BaseEntity {
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false)
    private Date createdAt;

    @JsonIgnore
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(insertable = false)
    private Date updatedAt;
    @JsonIgnore
    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;

}
