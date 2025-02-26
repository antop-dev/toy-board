package org.antop.board.post.mapper

import org.antop.board.common.extensions.pretty
import org.antop.board.post.dto.CommentView
import org.antop.board.post.model.Comment

fun Comment.toDto(): CommentView {
    val changed = (modified ?: created)
    return CommentView(
        id = id.value,
        content = content,
        changed = changed.pretty("yyyy.MM.dd HH:mm:ss"),
    )
}
