package com.hong.jpastudy.entity

import com.hong.jpastudy.dto.InsertNoticeDto
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.UpdateNoticeDto
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@NoArgsConstructor
open class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    open var id: Long = -1L,

    @Column(name = "title")
    open var title: String = "",

    @Column(name = "content")
    open var content: String = "",

    @Column(name = "start_at")
    open var startAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_at")
    open var endAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_by")
    open var updateBy: String = "",

    @Column(name = "create_by")
    open var createBy: String = "",

    @Column(name = "is_activated")
    open var isActivated: Boolean = true
) : BaseEntity() {

    companion object {
        private const val ID_FOR_INSERTION = -1L

        fun of(insertNoticeDto: InsertNoticeDto): Notice {
            return Notice(
                id = ID_FOR_INSERTION,
                title = insertNoticeDto.title,
                content = insertNoticeDto.content,
                startAt = insertNoticeDto.startAt,
                endAt = insertNoticeDto.endAt,
                updateBy = insertNoticeDto.createBy,
                createBy = insertNoticeDto.createBy,
                isActivated = insertNoticeDto.isActivated
            )
        }

        fun of(updateNoticeDto: UpdateNoticeDto): Notice {
            return Notice(
                id = updateNoticeDto.id,
                title = updateNoticeDto.title,
                content = updateNoticeDto.content,
                startAt = updateNoticeDto.startAt,
                endAt = updateNoticeDto.endAt,
                updateBy = updateNoticeDto.updateBy,
                createBy = updateNoticeDto.createBy,
                isActivated = updateNoticeDto.isActivated
            )
        }

    }

    fun toDataModel(): NoticeDto {
        return NoticeDto(
            id = id,
            title = title,
            content = content,
            startAt = startAt,
            endAt = endAt,
            updateBy = updateBy,
            createBy = createBy,
            isActivated = isActivated,
            isDelete = isDelete
        )
    }
}
