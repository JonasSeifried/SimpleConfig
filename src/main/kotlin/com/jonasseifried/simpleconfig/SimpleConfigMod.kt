package com.jonasseifried.simpleconfig

import com.jonasseifried.simpleconfig.SimpleConfigMod.MOD_ID
import kotlinx.serialization.Serializable
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SimpleConfigMod : ModInitializer {
    internal const val MOD_ID = "simpleconfig"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        MyConfig.patch(loadBefore = true) {
            test = 1.99999
            hello = "wowowow"
        }
        MyConfig.patch {
            test = 2.5
        }
        registerCommands()
        logger.info("Config Initialized")
    }
}

object MyConfig : SimpleConfig<MyConfig.MyData>(defaultValue = MyData()) {
    override var fileName = MOD_ID

    @Serializable
    class MyData {
        var test = 2.0
        var hello = "world"
    }
}