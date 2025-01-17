package io.collective

import java.time.Clock
import java.time.Instant

class SimpleAgedKache(private val clock: Clock = Clock.systemDefaultZone()) {

    private val cache = mutableMapOf<Any, CacheEntry>()

    data class CacheEntry(val value: Any, val expirationTime: Instant)

    fun put(key: Any, value: Any, retentionInMillis: Int) {
        val expirationTime = clock.instant().plusMillis(retentionInMillis.toLong())
        cache[key] = CacheEntry(value, expirationTime)
    }

    fun isEmpty(): Boolean {
        removeExpiredEntries()
        return cache.isEmpty()
    }

    fun size(): Int {
        removeExpiredEntries()
        return cache.size
    }

    fun get(key: Any): Any? {
        removeExpiredEntries()
        val entry = cache[key]
        return if (entry != null && entry.isValid(clock.instant())) entry.value else null
    }

    private fun removeExpiredEntries() {
        val currentTime = clock.instant()
        cache.entries.removeIf { !it.value.isValid(currentTime) }
    }

    private fun CacheEntry.isValid(currentTime: Instant): Boolean {
        return currentTime.isBefore(expirationTime)
    }
}

