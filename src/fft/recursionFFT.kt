package fft

import ComplexNumber
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun fftRecursion(x: Array<ComplexNumber>): Array<ComplexNumber> {
    val n = x.size

    if (n == 1)
        return arrayOf(x[0])

    require(n % 2 == 0) {
        "Размер FFT должен быть степенью двойки"
    }

    val even = fftRecursion(
        Array(n / 2) { x[it * 2] }
    )
    val odd = fftRecursion(
        Array(n / 2) { x[it * 2 + 1] }
    )

    val result = Array(n) {
        ComplexNumber(0.0, 0.0)
    }

    for (k in 0..< n / 2) {
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