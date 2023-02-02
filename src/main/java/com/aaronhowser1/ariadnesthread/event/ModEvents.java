package com.aaronhowser1.ariadnesthread.event;

import com.aaronhowser1.ariadnesthread.AriadnesThread;
import com.aaronhowser1.ariadnesthread.utils.ModScheduler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AriadnesThread.MOD_ID)
public class ModEvents {

    //From Tslat
    public static int tick;
    @SubscribeEvent
    public static void serverTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick++;
            ModScheduler.handleSyncScheduledTasks(tick);
        }
    }

}
