package com.hong.jpastudy.controller

import com.hong.jpastudy.dto.UserDto
import com.hong.jpastudy.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

    private final val MINUTE = 60
    private final val USER_ID = "userId"

    @GetMapping("/login")
    fun login(@ModelAttribute userDto: UserDto, request: HttpServletRequest): ResponseEntity<UserDto> {
        return if (userService.login(userDto)) {
            val session = request.session
            session.setAttribute(USER_ID, userDto.userId)
            session.maxInactiveInterval = 60 * MINUTE
            ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto)
        } else {
            ResponseEntity(userDto, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/logout")
    fun logout(@RequestParam userId: String, request: HttpServletRequest): ResponseEntity<Boolean> {
        val session = request.session

        return if (session.getAttribute(USER_ID) == null) {
            ResponseEntity(false, HttpStatus.BAD_REQUEST)
        } else {
            session.removeAttribute(USER_ID)
            ResponseEntity(true, HttpStatus.OK)
        }
    }

    @GetMapping("/register") // TODO("/register -> 다른 endpoint로!)
    fun checkDuplicateId(@RequestParam userId: String) : ResponseEntity<Boolean> {
        return if (userService.duplicateIdCheck(userId)) {
            ResponseEntity(false, HttpStatus.BAD_REQUEST)
        } else {
            ResponseEntity(true, HttpStatus.OK)
        }
    }

    @PostMapping
    fun register(@RequestBody userDto: UserDto): ResponseEntity<Boolean> {
        return ResponseEntity(userService.register(userDto), HttpStatus.OK)
    }

    @PutMapping
    fun changePassword(@RequestBody userDto: UserDto, request: HttpServletRequest): ResponseEntity<Boolean> {
        val session = request.session
        return if (userService.changePassword(userDto)) {
            session.removeAttribute(USER_ID)
            ResponseEntity(true, HttpStatus.OK)
        } else {
            ResponseEntity(false, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping
    fun deleteUser(@RequestBody userDto: UserDto, request: HttpServletRequest): ResponseEntity<Boolean> {
        val session = request.session
        return if (userService.deleteUser(userDto.userId)) {
            session.removeAttribute(USER_ID)
            ResponseEntity(true, HttpStatus.OK)
        } else {
            ResponseEntity(false, HttpStatus.NOT_FOUND)
        }
    }
}
