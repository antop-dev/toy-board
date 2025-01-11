package org.antop.board.common.extensions

import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable

fun <E> Collection<E>.toSizedIterable(): SizedIterable<E> = SizedCollection(this)
