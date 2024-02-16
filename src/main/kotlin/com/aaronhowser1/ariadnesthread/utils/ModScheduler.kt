package com.aaronhowser1.ariadnesthread.utils

import com.google.common.collect.HashMultimap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

object ModScheduler {

    var tick = 0
        set(value) {
            field = value
            handleSyncScheduledTasks(value)
        }

    private var scheduler: ScheduledExecutorService? = null
    private val scheduledSyncTasks = HashMultimap.create<Int, Runnable>()

    private fun scheduleSynchronisedTask(ticks: Int, run: Runnable) {
        scheduledSyncTasks.put(tick + ticks, run)
    }

    private fun scheduleAsyncTask(time: Int, unit: TimeUnit, run: Runnable) {
        if (scheduler == null) serverStartupTasks()
        scheduler!!.schedule(run, time.toLong(), unit)
    }

    private fun serverStartupTasks() {
        if (scheduler != null) scheduler!!.shutdownNow()
        scheduler = Executors.newScheduledThreadPool(1)
        handleSyncScheduledTasks(null)
    }

    private fun serverShutdownTasks() {
        handleSyncScheduledTasks(null)
        scheduler!!.shutdownNow()
        scheduler = null
    }

    private fun handleSyncScheduledTasks(tick: Int?) {
        if (scheduledSyncTasks.containsKey(tick)) {
            val tasks =
                if (tick == null) scheduledSyncTasks.values().iterator() else scheduledSyncTasks[tick].iterator()
            while (tasks.hasNext()) {
                try {
                    tasks.next().run()
                } catch (ex: Exception) {
//                    Logging.logMessage(Level.ERROR, "Unable to run unhandled scheduled task, skipping.", ex);
                }
                tasks.remove()
            }
        }
    }
}