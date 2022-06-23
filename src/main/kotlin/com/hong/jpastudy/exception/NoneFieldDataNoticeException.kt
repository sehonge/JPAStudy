package com.hong.jpastudy.exception

import com.hong.jpastudy.dto.UserDto

class NoneNoticeException(msg: String) : RuntimeException(msg)
class NoticeConvertException(msg: String) : RuntimeException(msg)
class NotValidUserForm(msg: String, userDto: UserDto) : RuntimeException(msg)
