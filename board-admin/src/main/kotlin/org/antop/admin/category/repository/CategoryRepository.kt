package org.antop.admin.category.repository

import io.github.openfeign.querydsl.jpa.spring.repository.QuerydslJpaRepository
import org.antop.admin.category.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository :
    JpaRepository<Category, Long>,
    QuerydslJpaRepository<Category, Long>
