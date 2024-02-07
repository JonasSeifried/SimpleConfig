package com.jonasseifried.simpleconfig

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.nio.file.Path
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties


abstract class SimpleConfigData {
    open val json = Json { prettyPrint = true; encodeDefaults = true }
    abstract var fileName: String
    open val path: Path = FabricLoader.getInstance().configDir

    private fun getFile(): File = path.resolve(fileName).toFile()

    fun <T : SimpleConfigData> T.patch(block: T.() -> Unit) = this.apply(block).also { it.store() }


    fun <T : SimpleConfigData> T.load() {
        if (!getFile().exists()) this.store()
        val configDataField = this.getConfigDataField()
        val configData = configDataField.call(this)!!
        val serializer = configData::class.createType().serializer()
        configDataField.toMutable().setter.call(this, json.decodeFromString(serializer, getFile().readText())!!)
    }

    fun <T : SimpleConfigData> T.store() {
        val configData = this.getConfigDataField().call(this)!!
        val serializer = configData::class.createType().serializer()
        getFile().writeText(json.encodeToString(serializer, configData))
    }

    private fun KType.serializer() = json.serializersModule.serializer(this)

    private fun <T> KProperty1<out T, *>.toMutable() = runCatching { this as KMutableProperty1<*, *> }.onFailure {
        it.apply {
            throw ClassCastException(
                "Make sure the field with the ${ConfigData::class.simpleName} Annotation is mutable ".plus(it.message)
            )
        }
    }.getOrThrow()

    private fun <T : SimpleConfigData> T.getConfigDataField() =
        this::class.declaredMemberProperties.firstOrNull() { it.annotations.contains(ConfigData()) }
            ?: throw IllegalStateException("Could not find a field in ${this::class.simpleName} with the ${ConfigData::class.simpleName} Annotation!")

}