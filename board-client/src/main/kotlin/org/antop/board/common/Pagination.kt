package org.antop.board.common

class Pagination {
    companion object {
        const val FIRST_PAGE = 1L
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_BLOCK_SIZE = 10
    }

    class Request(
        val page: Long = FIRST_PAGE,
        val size: Int = DEFAULT_PAGE_SIZE,
    )

    class Response<T>(
        val items: List<T>,
        val total: Long,
    )

    class Item<T>(
        /** 순번 */
        val seq: Long,
        val data: T,
    )

    class Ui<T>(
        items: List<T>,
        val page: Long = FIRST_PAGE,
        val size: Int = DEFAULT_PAGE_SIZE,
        val total: Long,
    ) {
        val items: List<Item<T>> =
            items.mapIndexed { i, it ->
                Item(
                    seq = total - ((page - 1) * size) - i,
                    data = it,
                )
            }

        /** 이전 페이지 여부 */
        val hasPrev: Boolean

        /** 이전 페이지 번호 */
        val prev: Long

        /** 다음 페이지 여부 */
        val hasNext: Boolean

        /** 다음 페이지 번호 */
        val next: Long

        /** 페이지 번호 목록 */
        val pages: List<Long>

        init {
            val blockSize = DEFAULT_BLOCK_SIZE
            var totalPage = total / size
            if (total % size > 0) totalPage++

            val startBlock = ((page - 1) / blockSize * blockSize) + 1
            var endBlock = startBlock + blockSize - 1
            if (endBlock > totalPage) endBlock = totalPage

            this.pages = (startBlock..endBlock).toList()

            if (startBlock > 1L) {
                this.hasPrev = true
                this.prev = startBlock - 1
            } else {
                this.hasPrev = false
                this.prev = -1
            }

            if (endBlock < totalPage) {
                this.hasNext = true
                this.next = endBlock + 1
            } else {
                this.hasNext = false
                this.next = -1
            }
        }
    }
}
