package com.jonasseifried.simpleconfig

import com.jonasseifried.simpleconfig.MyConfig.load
import com.jonasseifried.simpleconfig.MyConfig.patch
import kotlinx.serialization.Serializable
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SimpleConfigMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("simpleconfig")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		MyConfig.load()
		MyConfig.patch {
			this.config.run {
				this.test = 5.0
				this.hello = "minecraft"
			}
		}
		logger.info("Config Initialized")
	}
}


@Serializable
object MyConfig: SimpleConfigData() {
	override var fileName = "simpleconfig.json"

	@ConfigData
	var config: MyData = MyData()


	@Serializable
	class MyData{
		var test = 2.0
		var hello = "world"
	}
}