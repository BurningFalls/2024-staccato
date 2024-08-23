package com.woowacourse.staccato.presentation.mapper

import com.woowacourse.staccato.domain.model.Member
import com.woowacourse.staccato.domain.model.Memory
import com.woowacourse.staccato.domain.model.MemoryMoment
import com.woowacourse.staccato.presentation.common.MemberUiModel
import com.woowacourse.staccato.presentation.memory.model.MemoryUiModel
import com.woowacourse.staccato.presentation.memory.model.MemoryVisitUiModel

fun Memory.toUiModel() =
    MemoryUiModel(
        id = memoryId,
        title = memoryTitle,
        memoryThumbnailUrl = memoryThumbnailUrl,
        startAt = startAt,
        endAt = endAt,
        description = description,
        mates = mates.map { it.toUiModel() },
        visits = moments.map { it.toUiModel() },
    )

fun Member.toUiModel() =
    MemberUiModel(
        id = memberId,
        nickname = nickname,
        memberImage = memberImage,
    )

fun MemoryMoment.toUiModel() =
    MemoryVisitUiModel(
        id = momentId,
        placeName = placeName,
        visitImageUrl = momentImageUrl,
        visitedAt = visitedAt,
    )
