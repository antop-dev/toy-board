package org.antop.admin.category

data class CategoryDto(
    /** 카테고리ID */
    val id: Long,
    /** 카테고리명 */
    val name: String,
    /** 상태코드 */
    val statusCode: String,
    /** 상태명 */
    val statusName: String,
    /** 글쓰기 기능 사용 여부 */
    val featureWrite: Boolean,
    /** 비밀글 기능 사용 여부 */
    val featureSecret: Boolean,
    /** 파일 첨부 기능 사용 여부 */
    val featureFile: Boolean,
    /** 답글 기능 사용 여부 */
    val featureReply: Boolean,
    /** 댓글 기능 사용 여부 */
    val featureComment: Boolean,
    /** 투표(좋아요/싫어요 등) 기능 사용 여부 */
    val featureVote: Boolean,
    /** 공유 기능 사용 여부 */
    val featureShare: Boolean,
) {
    val featureNames: List<String>
        get() =
            mutableListOf<String>()
                .apply {
                    if (featureWrite) this += "글쓰기"
                    if (featureSecret) this += "비밀글"
                    if (featureFile) this += "파일첨부"
                    if (featureReply) this += "답글"
                    if (featureComment) this += "댓글"
                    if (featureVote) this += "투표"
                    if (featureShare) this += "공유"
                }.toList()
}
