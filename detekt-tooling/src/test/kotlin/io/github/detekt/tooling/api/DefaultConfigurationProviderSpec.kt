package io.github.detekt.tooling.api

import io.github.detekt.test.utils.createTempFileForTest
import io.github.detekt.tooling.api.spec.BaselineSpec
import io.github.detekt.tooling.api.spec.CompilerSpec
import io.github.detekt.tooling.api.spec.ConfigSpec
import io.github.detekt.tooling.api.spec.ExecutionSpec
import io.github.detekt.tooling.api.spec.ExtensionsSpec
import io.github.detekt.tooling.api.spec.LoggingSpec
import io.github.detekt.tooling.api.spec.ProcessingSpec
import io.github.detekt.tooling.api.spec.ProjectSpec
import io.github.detekt.tooling.api.spec.ReportsSpec
import io.github.detekt.tooling.api.spec.RulesSpec
import io.gitlab.arturbosch.detekt.api.Config
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.nio.file.Path

class DefaultConfigurationProviderSpec : Spek({

    describe("default configuration") {

        it("loads first found instance") {
            assertThatCode {
                DefaultConfigurationProvider.load(spec = Spec)
                    .copy(createTempFileForTest("test", "test"))
            }.doesNotThrowAnyException()
        }
    }
})

internal class TestConfigurationProvider : DefaultConfigurationProvider {
    override fun init(spec: ProcessingSpec) {
        // no-op
    }

    override fun get(): Config = Config.empty

    override fun copy(targetLocation: Path) {
        // nothing
    }
}

private object Spec : ProcessingSpec {
    override val baselineSpec: BaselineSpec
        get() = TODO("Not yet implemented")
    override val compilerSpec: CompilerSpec
        get() = TODO("Not yet implemented")
    override val configSpec: ConfigSpec
        get() = TODO("Not yet implemented")
    override val executionSpec: ExecutionSpec
        get() = TODO("Not yet implemented")
    override val extensionsSpec: ExtensionsSpec
        get() = TODO("Not yet implemented")
    override val rulesSpec: RulesSpec
        get() = TODO("Not yet implemented")
    override val loggingSpec: LoggingSpec
        get() = TODO("Not yet implemented")
    override val projectSpec: ProjectSpec
        get() = TODO("Not yet implemented")
    override val reportsSpec: ReportsSpec
        get() = TODO("Not yet implemented")
}
