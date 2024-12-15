package org.antop

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import org.jsoup.Jsoup

data class Post(
    val title: String,
    val content: String,
    val author: String,
    var createdAt: LocalDateTime,
)

// 디아블로 인벤 질문과 답변 게시판에서 30페이지 글,내용 긁어와서 INSERT 만들기
fun main() {
    val customFormat =
        LocalDateTime.Format {
            date(LocalDate.Formats.ISO)
            char(' ')
            hour()
            char(':')
            minute()
        }

    val posts =
        (30 downTo 1)
            .map { page ->
                Jsoup
                    .connect("https://www.inven.co.kr/board/diablo4/6034?p=$page")
                    .get()
                    .select("div.board-list table tbody tr td.tit a")
                    .reversed()
                    .map { el ->
                        val view = Jsoup.connect(el.absUrl("href")).get()
                        val title = view.selectFirst(".articleTitle")!!.text()
                        val content = view.selectFirst("#powerbbsContent")!!.html()
                        val author = view.selectFirst(".articleWriter")!!.text()
                        val createdAt = view.selectFirst(".articleDate")!!.text()
                        val c = Jsoup.parse(content.replace("<br>", "*br*"))
                        Post(
                            title = title.replace("'", "\""),
                            content =
                                c
                                    .text()
                                    .replace("*br*", "\n")
                                    .replace("'", "\"")
                                    .trim(),
                            author = author,
                            createdAt = LocalDateTime.parse(createdAt, customFormat),
                        )
                    }
            }.flatten()

    posts.forEach { post ->
        println(
            "INSERT INTO posts (subject, content, author, created) values ('${post.title}'," +
                " '${post.content}', '${post.author}', '${post.createdAt.format(LocalDateTime.Formats.ISO)}');",
        )
    }
}
