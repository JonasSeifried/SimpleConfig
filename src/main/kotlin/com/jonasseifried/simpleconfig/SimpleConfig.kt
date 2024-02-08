package com.jonasseifried.simpleconfig

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.nio.file.Path
import kotlin.reflect.full.createType

abstract class SimpleConfig<D : Any>(defaultValue: D) {
    open val json = Json { prettyPrint = true; encodeDefaults = true }
    abstract var fileName: String
    open val fileEnding: String = ".json"
    open val path: Path = FabricLoader.getInstance().configDir
    open val createReloadCommand = true

    init {
        registerInstance()
    }

    var data: D = defaultValue
        private set

    fun patch(loadBefore: Boolean = false, block: D.() -> Unit) {
        if (loadBefore) loadPersistedData()
        this.data.apply(block).also { persistData() }
    }

    fun loadPersistedData() {
        if (!getFile().exists()) persistData()
        data = json.decodeFromString(serializer(), getFile().readText())
    }

    fun persistData() = getFile().writeText(json.encodeToString(serializer(), data))

    private fun getFile(): File = path.resolve(fileName.plus(fileEnding)).toFile()

    @Suppress("UNCHECKED_CAST")
    private fun serializer() = json.serializersModule.serializer(type = data::class.createType()) as KSerializer<D>

    private fun registerInstance() {
        instances.add(this)
    }

    internal companion object {
        val instances: MutableSet<SimpleConfig<*>> = mutableSetOf()
    }
}

