package com.hong.jpastudy.controller

import com.hong.jpastudy.dto.InsertNoticeDto
import com.hong.jpastudy.dto.NoticeDto
import com.hong.jpastudy.dto.SearchNoticeDto
import com.hong.jpastudy.dto.UpdateNoticeDto
import com.hong.jpastudy.service.NoticeEditable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@RequestMapping("/api/notice")
class NoticeController(val noticeService: NoticeEditable) {

    @Operation(summary = "특정 공지사항 검색")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK")
    )
    @GetMapping("/search")
    fun getNoticeByQuery(@PageableDefault pageable: Pageable ,@ModelAttribute input: SearchNoticeDto): ResponseEntity<Page<NoticeDto>> {
        return ResponseEntity(noticeService.getNoticeByQuery(pageable, input), HttpStatus.OK)
    }

    @Operation(summary = "전체 공지사항 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK")
    )
    @GetMapping
    fun getAllNotice(@PageableDefault pageable: Pageable): ResponseEntity<Page<NoticeDto>> {
        return ResponseEntity(noticeService.getAllNotice(pageable), HttpStatus.OK)
    }

    @Operation(summary = "id에 대응되는 공지사항 검색")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "400", description = "noticeId에 대응되는 Notice가 없는경우, 404(NOT FOUND) 반환")
    )
    @GetMapping("/{id}")
    fun getNotice(@PathVariable("id") noticeId: Long): ResponseEntity<NoticeDto> {
        return ResponseEntity(noticeService.getNotice(noticeId), HttpStatus.OK)
    }

    @Operation(summary = "공지사항 추가")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "400", description = "Request Body에 정보를 모두 입력하지 않으면, 400(BAD REQUEST)을 반환")
    )
    @PostMapping
    fun addNotice(@RequestBody insertNoticeDto: InsertNoticeDto): ResponseEntity<Boolean> {
        return ResponseEntity(noticeService.addNotice(insertNoticeDto), HttpStatus.OK)
    }

    @Operation(summary = "공지사항 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "400", description = "Request Body에 정보를 모두 입력하지 않으면, 400(BAD REQUEST)을 반환")
    )
    @PutMapping
    fun updateNotice(@RequestBody updateNoticeDto: UpdateNoticeDto): ResponseEntity<Boolean> {
        return ResponseEntity(noticeService.updateNotice(updateNoticeDto), HttpStatus.OK)
    }

    @Operation(summary = "공지사항 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "OK"),
        ApiResponse(responseCode = "404", description = "noticeId에 대응되는 Notice가 없는경우, 404(NOT FOUND) 반환")
    )
    @DeleteMapping("/{id}")
    fun deleteNotice(@PathVariable("id") noticeId: Long): ResponseEntity<Boolean> {
        return ResponseEntity(noticeService.deleteNotice(noticeId), HttpStatus.OK)
    }
}
