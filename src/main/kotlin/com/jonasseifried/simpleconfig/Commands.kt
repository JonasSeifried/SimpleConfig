package com.jonasseifried.simpleconfig

import com.jonasseifried.simpleconfig.SimpleConfigMod.MOD_ID
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text


fun registerCommands() {
    CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, registryAccess, environment ->
        val literal = literal(MOD_ID).baseCommand().loadAllPersistedDataSubCommand()


        SimpleConfig.instances.filter { it.createReloadCommand }.forEach { config ->
            literal.loadPersistedDataCommand(config = config)
        }
        dispatcher.register(literal)
    })
}

private fun LiteralArgumentBuilder<ServerCommandSource>.baseCommand() = this.executes { context ->
    context.source.sendFeedback(
        { Text.translatable("command.simpleconfig.simpleconfig.feedback") }, false
    )
    1
}

private fun LiteralArgumentBuilder<ServerCommandSource>.loadAllPersistedDataSubCommand() =
    this.then(literal(Text.translatable("command.simpleconfig.simpleconfig.all").string).executes { context ->
        SimpleConfig.instances.filter { it.createReloadCommand }.forEach { it.loadPersistedData() }
        context.source.sendFeedback(
            { Text.translatable("command.simpleconfig.simpleconfig.all.feedback") }, false
        )
        1
    })


private fun LiteralArgumentBuilder<ServerCommandSource>.loadPersistedDataCommand(config: SimpleConfig<*>) =
    this.then(literal(config.fileName).executes { context ->
        context.source.sendFeedback({
            Text.translatable(
                "command.simpleconfig.simpleconfig.any_config.feedback", config.fileName
            )
        }, false)
        config.loadPersistedData()
        1
    })


