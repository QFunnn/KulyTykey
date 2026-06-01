import kotlin.math.*

data class ComplexNumber(
    val re: Double,
    val im: Double
) {
    fun plus(other: ComplexNumber) =
        ComplexNumber(re + other.re, im + other.im)

    fun minus(other: ComplexNumber) =
        ComplexNumber(re - other.re, im - other.im)

    fun times(other: ComplexNumber) =
        ComplexNumber(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        )
}

fun main() {
    val x = mutableListOf(
        ComplexNumber(1.0, 0.0),
        ComplexNumber(2.0, 0.0),
        ComplexNumber(3.0, 0.0),
        ComplexNumber(4.0, 0.0),
        ComplexNumber(0.0, 0.0),
        ComplexNumber(0.0, 0.0),
        ComplexNumber(0.0, 0.0),
        ComplexNumber(0.0, 0.0)
    )

    val n = x.size
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
        for (k in 0..<l / 2) {
            val angle = -2.0 * Math.PI * k / l
            val w = ComplexNumber(cos(angle), sin(angle))

            for (j in 0..<n / l) {
                val index1 = j * l + k
                val index2 = index1 + l / 2

                val tao = w.times(x[index2])

                x[index2] = x[index1].minus(tao)
                x[index1] = x[index1].plus(tao)
            }
        }
        l *= 2
    }

    println(x)
}