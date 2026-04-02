package com.example.b01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass //이 클래스를 상속받는 엔티티는 regDate, modDate 컬럼이 자동으로 생긴다
@EntityListeners(value = { AuditingEntityListener.class}) //날짜를 직접 넣어주지 않아도 자동으로 관리된다
@Getter
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "regdate" , updatable = false) //false는 한번 값이 들어가면 수정이 불가능
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}
