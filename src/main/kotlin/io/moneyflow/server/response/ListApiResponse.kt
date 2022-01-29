package io.moneyflow.server.response

data class ListApiResponse<E>(
    private val items: List<E>,
    val totalCount: Long,
) : ApiResponse()