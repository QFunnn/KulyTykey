import java.io.File
import javax.sound.sampled.AudioSystem
import kotlin.math.*

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

fun fft(x: Array<ComplexNumber>): Array<ComplexNumber> {
    val n = x.size

    if (n == 1)
        return arrayOf(x[0])

    require(n % 2 == 0) {
        "Размер FFT должен быть степенью двойки"
    }

    val even = fft(
        Array(n / 2) { x[it * 2] }
    )
    val odd = fft(
        Array(n / 2) { x[it * 2 + 1] }
    )

    val result = Array(n) {
        ComplexNumber(0.0, 0.0)
    }

    for (k in 0..<n / 2) {
        val angle = -2.0 * PI * k / n
        val w = ComplexNumber(
            cos(angle),
            sin(angle)
        )

        val t = w * odd[k]
        result[k] = even[k] + t
        result[k + n / 2] = even[k] - t
    }

    return result
}

fun main() {
    // Получение семплов из источника.
    // В данном случае в test.wav постоянный сигнал в 440Hz
    val (samples, samplesRate) = readWavSamples("test.wav")
    println(samples.size)

    fun hann(size: Int): DoubleArray {
        return DoubleArray(size) { n ->
            0.5 * (1.0 - cos(
                2.0 * PI * n / (size - 1)
            ))
        }
    }

    val N = 2048 // Размер окна FFT
    require(samples.size >= N)

    val frame = samples.copyOfRange(0, N) // Создание окна FFT
    val window = hann(N) // Применения оконнной функции Хэнна

    for (i in 0..< N) {
        frame[i] *= window[i] // Применения результата функции Хэнна к фрейму
    }

    val input = Array(N) {
        ComplexNumber(frame[it], 0.0)
    }

    val spectrum = fft(input)

    for (i in 0..< N / 2) {
        val magnitude = sqrt(
            spectrum[i].re * spectrum[i].re +
            spectrum[i].im * spectrum[i].im
        )

        val freq = i * samplesRate / N
        println(
            "%.2f Hz -> %.4f".format(
                freq, magnitude
            )
        )
    }


}