package org.antop.board.post.service

import kotlinx.datetime.LocalDateTime
import org.antop.board.common.extensions.now
import org.antop.board.common.extensions.set
import org.antop.board.post.dto.PostDto
import org.antop.board.post.model.Post
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class PostHitsService(
    private val redisTemplate: RedisTemplate<String, LocalDateTime>,
) {
    /**
     * 조회 수 증가
     *
     * @param post 게시글 DTO
     * @param visitorId 식별자 (IP 등)
     * @return 증가된(또는 증가 안된) 조회 수
     */
    @Transactional
    fun incrementHits(
        post: PostDto,
        visitorId: String,
    ): Long {
        val key = makeKey(post.id, visitorId)
        return redisTemplate.opsForValue()[key]?.run {
            post.hits
        } ?: run {
            val updated =
                Post
                    // select for update
                    .findByIdAndUpdate(post.id) {
                        // 조회수 증가
                        it.hits++
                        // 조회수 증가 후 24시간 동안 조회 수 증가 안됨
                        redisTemplate.opsForValue()[key, 1, TimeUnit.DAYS] = LocalDateTime.now()
                    }
            updated?.hits ?: post.hits
        }
    }

    /** 레디스 키 만들기 */
    private fun makeKey(
        id: Long,
        visitorId: String,
    ): String = "post:$id:$visitorId"
}
