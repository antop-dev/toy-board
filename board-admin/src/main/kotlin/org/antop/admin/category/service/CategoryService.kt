package org.antop.admin.category.service

import org.antop.admin.category.dto.CategoryDto
import org.antop.admin.category.dto.CategorySaveDto
import org.antop.admin.category.model.Category
import org.antop.admin.category.model.CategoryStatus
import org.antop.admin.category.model.QCategory.category
import org.antop.admin.category.repository.CategoryRepository
import org.antop.admin.common.exceptions.CategoryNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
) {
    fun getList(
        name: String? = null,
        statusCode: String? = null,
    ): List<CategoryDto> =
        categoryRepository
            .selectFrom(category)
            .apply {
                if (!name.isNullOrBlank()) {
                    where(category.name.contains(name))
                }
                if (!statusCode.isNullOrBlank()) {
                    val status = CategoryStatus.from(statusCode)
                    where(category.status.eq(status))
                }
            }.orderBy(category.id.desc())
            .fetch()
            .map { it.toDto() }

    fun getCategory(categoryId: Long): CategoryDto =
        categoryRepository.findByIdOrNull(categoryId)?.toDto()
            ?: throw CategoryNotFoundException(categoryId)

    @Transactional
    fun register(category: CategorySaveDto) {
        val entity =
            Category(
                name = category.name,
                status = CategoryStatus.from(category.statusCode),
                featureWrite = category.featureWrite,
                featureSecret = category.featureSecret,
                featureFile = category.featureFile,
                featureReply = category.featureReply,
                featureComment = category.featureComment,
                featureVote = category.featureVote,
                featureShare = category.featureShare,
            )
        categoryRepository.save(entity)
    }

    @Transactional
    fun modify(
        categoryId: Long,
        category: CategorySaveDto,
    ) {
        categoryRepository.findByIdOrNull(categoryId)?.let {
            it.name = category.name
            it.status = CategoryStatus.from(category.statusCode)
            it.featureWrite = category.featureWrite
            it.featureSecret = category.featureSecret
            it.featureFile = category.featureFile
            it.featureReply = category.featureReply
            it.featureComment = category.featureComment
            it.featureVote = category.featureVote
            it.featureShare = category.featureShare
        }
    }

    private fun Category.toDto() =
        CategoryDto(
            id = id,
            name = name,
            statusCode = status.code,
            statusName = status.korName,
            featureWrite = featureWrite,
            featureSecret = featureSecret,
            featureFile = featureFile,
            featureReply = featureReply,
            featureComment = featureComment,
            featureVote = featureVote,
            featureShare = featureShare,
        )
}
