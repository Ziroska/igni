package ru.igni.manager.ui

internal const val COAL_ATTENTION_INTERVAL_MS = 22 * 60 * 1000L

internal data class HallAttentionItem(
    val tableNumber: Int,
    val hookahIndex: Int,
    val guestName: String,
    val overdueMs: Long,
    val elapsedMs: Long
)

internal fun buildHallAttentionItems(
    sessions: Map<Int, TableSession>,
    now: Long,
    intervalMs: Long = COAL_ATTENTION_INTERVAL_MS
): List<HallAttentionItem> = sessions.flatMap { (tableNumber, session) ->
    session.hookahs.mapIndexedNotNull { hookahIndex, hookah ->
        val elapsed = (now - hookah.lastCoalAt).coerceAtLeast(0L)
        if (elapsed < intervalMs) return@mapIndexedNotNull null
        HallAttentionItem(
            tableNumber = tableNumber,
            hookahIndex = hookahIndex,
            guestName = session.guestName,
            overdueMs = elapsed - intervalMs,
            elapsedMs = elapsed
        )
    }
}.sortedWith(
    compareByDescending<HallAttentionItem> { it.overdueMs }
        .thenBy { it.tableNumber }
        .thenBy { it.hookahIndex }
)

internal fun formatAttentionMinutes(ms: Long): Int = (ms.coerceAtLeast(0L) / 60_000L).toInt()
