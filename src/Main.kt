import kotlin.math.hypot

data class ComplexNumber(
    val re: Double,
    val im: Double
) {
    fun abs(): Double = hypot(re, im)

    fun plus(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            re + other.re,
            im + other.im
        )

    fun minus(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            re - other.re,
            im - other.im
        )

    fun times(other: ComplexNumber): ComplexNumber =
        ComplexNumber(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        )
}

data class FrequencyBin(
    val amplitude: Double,
    val frequency: Double
)
data class Frame(
    val startMs: Double,
    val endMs: Double,
    val bins: Collection<FrequencyBin>
)
data class Result(
    val frames: Collection<Frame>
)

fun main() {

}