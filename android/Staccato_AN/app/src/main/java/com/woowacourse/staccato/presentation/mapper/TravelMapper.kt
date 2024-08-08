package com.woowacourse.staccato.presentation.mapper

import com.woowacourse.staccato.domain.model.Member
import com.woowacourse.staccato.domain.model.Travel
import com.woowacourse.staccato.domain.model.TravelVisit
import com.woowacourse.staccato.presentation.common.MemberUiModel
import com.woowacourse.staccato.presentation.travel.model.TravelUiModel
import com.woowacourse.staccato.presentation.travel.model.TravelVisitUiModel

fun Travel.toUiModel() =
    TravelUiModel(
        id = travelId,
        title = travelTitle,
        travelThumbnailUrl = travelThumbnailUrl,
        startAt = startAt,
        endAt = endAt,
        description = description,
        mates = mates.map { it.toUiModel() },
        visits = visits.map { it.toUiModel() },
    )

fun Member.toUiModel() =
    MemberUiModel(
        id = memberId,
        nickname = nickname,
        memberImage = memberImage,
    )

fun TravelVisit.toUiModel() =
    TravelVisitUiModel(
        id = visitId,
        placeName = placeName,
        visitImageUrl = visitImageUrl,
        visitedAt = visitedAt,
    )
