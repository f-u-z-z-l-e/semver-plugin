import model.BuildMetadata
import model.PreRelease
import model.Version
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ToStringVersionTest {

    @Test
    fun toStringTest() {
        val rc1 = PreRelease("rc1")
        val buildMetadata1 = BuildMetadata("25c2427")

        val version = Version(major = 1, minor = 0, patch = 1)
        val versionWithPreRelease = Version(major = 1, minor = 0, patch = 1, preRelease = rc1)
        val versionWithBuildMetadata = Version(major = 1, minor = 0, patch = 1, buildMetadata = buildMetadata1)
        val versionWithPreReleaseAndBuildMetadata = Version(major = 1, minor = 0, patch = 1, preRelease = rc1, buildMetadata = buildMetadata1)

        Assertions.assertEquals("1.0.1", version.toString())
        Assertions.assertEquals("1.0.1-rc1", versionWithPreRelease.toString())
        Assertions.assertEquals("1.0.1+25c2427", versionWithBuildMetadata.toString())
        Assertions.assertEquals("1.0.1-rc1+25c2427", versionWithPreReleaseAndBuildMetadata.toString())
    }
}