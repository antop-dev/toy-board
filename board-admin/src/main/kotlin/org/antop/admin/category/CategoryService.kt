package org.antop.admin.category

import org.antop.admin.common.exceptions.CategoryNotFoundException
import org.antop.admin.jooq.tables.records.CategoriesRecord
import org.antop.admin.jooq.tables.references.CATEGORIES
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val dslContext: DSLContext,
) {
    fun getList(
        name: String? = null,
        statusCode: String? = null,
    ): List<CategoryDto> =
        dslContext
            .selectFrom(CATEGORIES)
            .apply {
                name?.let {
                    where(CATEGORIES.CATEGORY_NAME.like("%$it%"))
                }
                statusCode?.let {
                    where(CATEGORIES.STATUS_CODE.eq(CategoryStatus.from(it)))
                }
            }.orderBy(CATEGORIES.CATEGORY_ID.desc())
            .fetch()
            .map { it.toDto() }

    fun getCategory(categoryId: Long): CategoryDto =
        dslContext
            .selectFrom(CATEGORIES)
            .where(CATEGORIES.CATEGORY_ID.eq(categoryId))
            .fetchOne()
            ?.toDto() ?: throw IllegalArgumentException("Category not found with id: $categoryId")

    private fun CategoriesRecord.toDto() =
        CategoryDto(
            id = categoryId!!,
            name = categoryName,
            statusCode = statusCode?.code ?: "",
            statusName = statusCode?.korName ?: "",
            featureWrite = featureWrite ?: false,
            featureSecret = featureSecret ?: false,
            featureFile = featureFile ?: false,
            featureReply = featureReply ?: false,
            featureComment = featureComment ?: false,
            featureVote = featureVote ?: false,
            featureShare = featureShare ?: false,
        )

    @Transactional
    fun register(category: CategorySaveDto) {
        val record = dslContext.newRecord(CATEGORIES)
        record.applyFormData(category).store()
    }

    @Transactional
    fun modify(
        categoryId: Long,
        category: CategorySaveDto,
    ) {
        val record =
            dslContext
                .selectFrom(CATEGORIES)
                .where(CATEGORIES.CATEGORY_ID.eq(categoryId))
                .fetchOne()
        if (record == null) {
            throw CategoryNotFoundException(categoryId)
        }
        record.applyFormData(category).store()
    }

    private fun CategoriesRecord.applyFormData(category: CategorySaveDto): CategoriesRecord {
        categoryName = category.name
        statusCode = CategoryStatus.from(category.statusCode)
        featureWrite = category.featureWrite
        featureSecret = category.featureSecret
        featureFile = category.featureFile
        featureReply = category.featureReply
        featureComment = category.featureComment
        featureVote = category.featureVote
        featureShare = category.featureShare
        return this
    }
}
