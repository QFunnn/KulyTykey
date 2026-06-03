
import fft.fftIterational
import fft.fftRecursion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.system.measureNanoTime

fun main() {
    val (samples, samplesRate) = readWavSamples("test.wav")
    println(samples)

    fun hann(size: Int): DoubleArray =
        DoubleArray(size) { n ->
            0.5 * (1.0 - cos(
                2.0 * PI * n / (size - 1)
            ))
        }

    fun printSpectrum(
        title: String,
        spectrum: Array<ComplexNumber>,
        sampleRate: Float,
        n: Int
    ) {
        println("\n=== $title ===")

        for (i in 0 ..< n / 2) {

            val magnitude = sqrt(
                spectrum[i].re * spectrum[i].re +
                        spectrum[i].im * spectrum[i].im
            )

            val freq = i * sampleRate / n

            println(
                "%.2f Hz -> %.4f".format(
                    freq,
                    magnitude
                )
            )
        }
    }

    val N = 2048 // степень двойки
    require(samples.size >= N)

    val frame = samples.copyOfRange(0, N)
    val window = hann(N)

    for (i in 0 ..< N) {
        frame[i] *= window[i]
    }

    val input = Array(N) {
        ComplexNumber(frame[it], 0.0)
    }

    lateinit var spectrumRecursion: Array<ComplexNumber>
    val recursionTime = measureNanoTime {
        spectrumRecursion = fftRecursion(input)
    }

    lateinit var spectrumIterational: Array<ComplexNumber>
    val iterativeTime = measureNanoTime {
        spectrumIterational = fftIterational(input)
    }

    printSpectrum(
        "RECURSIVE FFT",
        spectrumRecursion,
        samplesRate,
        N
    )

    printSpectrum(
        "ITERATIVE FFT",
        spectrumIterational,
        samplesRate,
        N
    )

    println(
        "Recursive FFT : %.3f ms".format(
            recursionTime / 1_000_000.0
        )
    )

    println(
        "Iterative FFT : %.3f ms".format(
            iterativeTime / 1_000_000.0
        )
    )

    println(
        "Speedup: %.2fx".format(
            recursionTime.toDouble() / iterativeTime
        )
    )


    fun dft(x: Array<ComplexNumber>): Array<ComplexNumber> {
        val N = x.size

        return Array(N) { k ->
            var result = ComplexNumber(0.0, 0.0)

            for (n in 0..N-1) {
                val angle = -2.0 * PI * k * n / N // e^ix = cos(x) + isin(x)
                val w = ComplexNumber(
                    cos(angle),
                    sin(angle)
                )

                result += x[n] * w
            }

            result
        }
    }

    lateinit var spectrumDFT: Array<ComplexNumber>
    val dftTIme = measureNanoTime {
        spectrumDFT = dft(input)
    }

    println(
        "FFT : %.3f ms".format(
            dftTIme / 1_000_000.0
        )
    )
    printSpectrum("test", spectrumDFT, samplesRate, N)


    //
}