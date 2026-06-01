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

fun bitReverse(x: Int, log2n: Int): Int {
    var n = x
    var res = 0
    repeat(log2n) {
        res = (res shl 1) or (n and 1)
        n = n shr 1
    }
    return res
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

    println(x)
}