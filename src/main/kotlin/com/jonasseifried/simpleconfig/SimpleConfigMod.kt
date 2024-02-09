package com.jonasseifried.simpleconfig

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SimpleConfigMod : ModInitializer {
    internal const val MOD_ID = "simpleconfig"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        registerCommands()
        logger.info("Config Initialized")
    }
}