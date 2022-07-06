package com.hong.jpastudy.exception

class NoneNoticeException(msg: String) : RuntimeException(msg)
class NoticeConvertException(msg: String) : RuntimeException(msg)
class NotValidUserForm(msg: String) : RuntimeException(msg)
class UserNotFoundException(msg: String) : RuntimeException(msg)
