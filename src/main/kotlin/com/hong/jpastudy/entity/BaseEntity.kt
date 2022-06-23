package com.hong.jpastudy.entity

import lombok.NoArgsConstructor
import javax.persistence.MappedSuperclass

@MappedSuperclass
@NoArgsConstructor
open class BaseEntity(
    var isDelete: Boolean = false
) {

    fun delete() {
        isDelete = true;
    }
}
