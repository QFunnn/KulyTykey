import java.io.File
import javax.sound.sampled.AudioSystem

fun readWavSamples(path: String): Pair<DoubleArray, Float> {
    val stream = AudioSystem.getAudioInputStream(File(path))
    val format = stream.format

    require(format.sampleSizeInBits == 16) {
        "Принимается только 16-бит PCM"
    }

    val bytes = stream.readBytes()
    val samples = DoubleArray(bytes.size / 2)

    var samplesIndex = 0

    for (i in bytes.indices step 2) {
        val low = bytes[i].toInt() and 0xFF
        val high = bytes[i + 1].toInt()

        val value = (high shl 8) or low
        samples[samplesIndex++] = value / 32768.0
    }

    return Pair(
        samples,
        format.sampleRate
    )
}

data class ComplexNumber(
    val re: Double,
    val im: Double
) {
    operator fun plus(other: ComplexNumber) =
        ComplexNumber(re + other.re, im + other.im)

    operator fun minus(other: ComplexNumber) =
        ComplexNumber(re - other.re, im - other.im)

    operator fun times(other: ComplexNumber) =
        ComplexNumber(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        )
}