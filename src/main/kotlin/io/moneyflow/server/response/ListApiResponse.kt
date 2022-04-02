package io.moneyflow.server.response

data class ListApiResponse<E>(
    val items: List<E>,
    val totalCount: Long,
) : ApiResponse()