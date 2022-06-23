package com.hong.jpastudy.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Period(
    @Column(name = "start_at")
    var startAt: LocalDateTime,

    @Column(name = "end_at")
    var endAt: LocalDateTime
) {
}
