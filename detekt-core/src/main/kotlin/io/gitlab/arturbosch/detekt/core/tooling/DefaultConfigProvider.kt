package io.gitlab.arturbosch.detekt.core.tooling

import io.github.detekt.tooling.api.DefaultConfigurationProvider
import io.github.detekt.tooling.api.spec.ProcessingSpec
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.core.config.YamlConfig
import io.gitlab.arturbosch.detekt.core.settings.ExtensionFacade
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DefaultConfigProvider : DefaultConfigurationProvider {
    private lateinit var spec: ProcessingSpec

    override fun init(spec: ProcessingSpec) {
        this.spec = spec
    }

    override fun get(): Config = spec.getDefaultConfiguration()

    override fun copy(targetLocation: Path) {
        Files.copy(configInputStream(spec), targetLocation, StandardCopyOption.REPLACE_EXISTING)
    }
}

private fun configInputStream(spec: ProcessingSpec): InputStream {
    val outputStream = PipedOutputStream()

    Thread {
        requireNotNull(spec.javaClass.getResourceAsStream("/default-detekt-config.yml"))
            .use { it.copyTo(outputStream) }

        ExtensionFacade(spec.extensionsSpec).pluginLoader
            .getResourcesAsStream("config/config.yml")
            .forEach { inputStream ->
                outputStream.bufferedWriter().append('\n').flush()
                inputStream.use { it.copyTo(outputStream) }
            }

        outputStream.close()
    }.start()

    return PipedInputStream(outputStream)
}

private fun ClassLoader.getResourcesAsStream(name: String): Sequence<InputStream> {
    return getResources(name)
        .asSequence()
        .map { it.openStream() }
}

fun ProcessingSpec.getDefaultConfiguration(): Config {
    return YamlConfig.load(configInputStream(this).reader())
}
