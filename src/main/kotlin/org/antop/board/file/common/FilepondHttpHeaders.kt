package org.antop.board.file.common

/**
 * Filepond 요청/응답 해더
 */
object FilepondHttpHeaders {
    /** 로드 : 파일 ID */
    const val TRANSFER_ID = "X-Content-Transfer-Id"

    /** 청크 업로드 : 파일명 */
    const val UPLOAD_NAME = "Upload-Name"

    /** 청크 초기화 : 파일 타입 */
    const val UPLOAD_TYPE = "Upload-Type"

    /** 청크 초기화 : 파일 크기 */
    const val UPLOAD_LENGTH = "Upload-Length"

    /** 청크 업로드 요청 : 청크 크기 */
    const val UPLOAD_OFFSET = "Upload-Offset"
}
