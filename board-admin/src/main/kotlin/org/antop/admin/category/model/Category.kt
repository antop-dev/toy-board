package org.antop.admin.category.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.antop.admin.common.jpa.Auditable

@Entity
@Table(name = "categories")
class Category(
    /** 게시판명 */
    @Column(name = "category_name")
    var name: String,
    /** 게시판 상태 */
    @Column(name = "status_code")
    var status: CategoryStatus = CategoryStatus.ACTIVE,
    /** 글쓰기 기능 사용 여부 */
    @Column(name = "feature_write")
    var featureWrite: Boolean = true,
    /** 비밀글 기능 사용 여부 */
    @Column(name = "feature_secret")
    var featureSecret: Boolean = true,
    /** 파일 업로드 기능 사용 여부 */
    @Column(name = "feature_file")
    var featureFile: Boolean = true,
    /** 답글 기능 사용 여부 */
    @Column(name = "feature_reply")
    var featureReply: Boolean = true,
    /** 댓글 기능 사용 여부 */
    @Column(name = "feature_comment")
    var featureComment: Boolean = true,
    /** 투표(좋아요, 싫어요) 기능 사요 여부 */
    @Column(name = "feature_vote")
    var featureVote: Boolean = true,
    /** 공유 기능 사용 여부 */
    @Column(name = "feature_share")
    var featureShare: Boolean = true,
) : Auditable() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    val id: Long = 0
}
