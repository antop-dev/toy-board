package org.antop.board.member.exception

class MemberNotFoundException(
    email: String,
) : RuntimeException(email)
