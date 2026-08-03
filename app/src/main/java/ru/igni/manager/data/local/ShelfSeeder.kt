package ru.igni.manager.data.local

import androidx.room.withTransaction

object ShelfSeeder {
    suspend fun seedIfEmpty(database: IgniDatabase) {
        val dao = database.shelfDao()
        if (dao.countBrands() > 0) return

        database.withTransaction {
            if (dao.countBrands() > 0) return@withTransaction

            val brandIds = linkedMapOf<String, Long>()
            INITIAL_SHELF_ITEMS.forEach { item ->
                val brandKey = item.brand
                val brandId = brandIds[brandKey] ?: dao.insertBrand(
                    TobaccoBrandEntity(
                        name = brandKey,
                        densityCoefficient = 1.0
                    )
                ).also { brandIds[brandKey] = it }
                val flavorId = dao.insertFlavor(
                    TobaccoFlavorEntity(
                        brandId = brandId,
                        name = item.flavor,
                        descriptor = item.descriptor,
                        isAvailable = item.available
                    )
                )
                dao.insertContainer(
                    TobaccoContainerEntity(
                        flavorId = flavorId,
                        label = "Стартовый остаток",
                        remainingGrams = item.stockGrams.coerceAtLeast(0.0),
                        isActive = item.stockGrams > 0.0
                    )
                )
            }
        }
    }
}
