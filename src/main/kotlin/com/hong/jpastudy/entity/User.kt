package com.hong.jpastudy.entity

import com.hong.jpastudy.dto.UserDto
import lombok.NoArgsConstructor
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@NoArgsConstructor
open class User(
    @Id @Column(name = "user_id")
    open var id: String = "",
    open var password: String = "",
) : BaseEntity() {
    fun toDataModel(): UserDto {
        return UserDto(
            userId = id,
            password = password
        )
    }
}
