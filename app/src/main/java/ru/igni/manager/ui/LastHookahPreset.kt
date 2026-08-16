package ru.igni.manager.ui

import ru.igni.manager.data.local.CrmDao
import ru.igni.manager.data.local.MixFlavorOption

internal data class LastHookahPreset(
    val bowlType: String,
    val strength: Int,
    val mix: List<Pair<MixFlavorOption, Int>>,
    val unavailableFlavors: List<String>
) {
    val canApply: Boolean
        get() = mix.isNotEmpty() && unavailableFlavors.isEmpty() && mix.sumOf { it.second } == 100
}

internal suspend fun loadLastHookahPreset(
    crmDao: CrmDao,
    guestId: Long,
    mixOptions: List<MixFlavorOption>
): LastHookahPreset? {
    var lastHookah = null as ru.igni.manager.data.local.HookahHistoryEntity?
    for (visit in crmDao.getVisits(guestId)) {
        lastHookah = crmDao.getHookahsForVisit(visit.id).maxByOrNull { it.startedAt }
        if (lastHookah != null) break
    }
    val resolvedHookah = lastHookah ?: return null

    val historyItems = crmDao.getMixItemsForHookah(resolvedHookah.id)
    if (historyItems.isEmpty()) return null

    val unavailable = mutableListOf<String>()
    val mapped = historyItems.mapNotNull { item ->
        val option = item.flavorId?.let { flavorId ->
            mixOptions.firstOrNull { it.flavorId == flavorId }
        } ?: mixOptions.firstOrNull {
            it.brandName.equals(item.brandNameSnapshot, ignoreCase = true) &&
                it.flavorName.equals(item.flavorNameSnapshot, ignoreCase = true)
        }

        if (option == null) {
            unavailable += "${item.brandNameSnapshot} • ${item.flavorNameSnapshot}"
            null
        } else {
            option to item.percentage
        }
    }

    return LastHookahPreset(
        bowlType = resolvedHookah.bowlType,
        strength = resolvedHookah.strength,
        mix = mapped,
        unavailableFlavors = unavailable.distinct()
    )
}
