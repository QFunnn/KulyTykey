package fft

import ComplexNumber
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun fftIterational(input: Array<ComplexNumber>): Array<ComplexNumber> {
    val x = input.copyOf()

    val n = x.size

    if (n == 1)
        return arrayOf(x[0])

    require(n % 2 == 0) {
        "Размер FFT должен быть степенью двойки"
    }

    val shift = 1 + Integer.numberOfLeadingZeros(n)
    for (k in 0..< n) {
        val j = Integer.reverse(k) ushr shift

        if (j > k) {
            val tmp = x[j]
            x[j] = x[k]
            x[k] = tmp
        }
    }

    var l = 2

    while (l <= n) {
        for (k in 0 ..< l / 2) {
            val angle = -2.0 * PI * k / l
            val w = ComplexNumber(
                cos(angle),
                sin(angle)
            )

            for (j in 0 ..< n / l) {
                val index1 = j * l + k
                val index2 = index1 + l / 2

                val tao = w * x[index2]

                val u = x[index1]
                val v = w * x[index2]

                x[index1] = u + v
                x[index2] = u - v
            }
        }

        l *= 2
    }

    return x
}
