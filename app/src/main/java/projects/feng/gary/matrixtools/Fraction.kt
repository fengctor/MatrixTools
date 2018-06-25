package projects.feng.gary.matrixtools


class Fraction {
    private val numerator: Int
    private val denominator: Int

    constructor(numer: Int, denom: Int) {
        val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    constructor(integer: Int) {
        numerator = integer
        denominator = 1
    }

    constructor(stringRep: String) {
        val fracParts = stringRep.split("/".toRegex())

        val numer = fracParts.get(0).toInt()
        val denom = if (fracParts.size == 1) 1 else fracParts.get(1).toInt()

        val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    operator fun plus(other: Fraction): Fraction {
        val newNumer = this.numerator * other.denominator + other.numerator * this.denominator
        val newDenom = this.denominator * other.denominator

        return Fraction(newNumer, newDenom)
    }

    operator fun minus(other: Fraction): Fraction {
        return this + -other
    }

    operator fun times(other: Fraction): Fraction {
        val newNumer = this.numerator * other.numerator
        val newDenom = this.denominator * other.denominator

        return Fraction(newNumer, newDenom)
    }

    operator fun div(other: Fraction): Fraction {
        return this * other.reciprocal()
    }

    operator fun unaryMinus(): Fraction {
        return Fraction(-this.numerator, this.denominator)
    }

    fun reciprocal(): Fraction {
        return Fraction(this.denominator, this.numerator)
    }


    override fun equals(other: Any?): Boolean {
        return other is Fraction
                && other.numerator == this.numerator
                && other.denominator == this.denominator
    }

    override fun toString(): String {
        return if (this.denominator == 1)
            this.numerator.toString()
        else
            this.numerator.toString() + "/" + this.denominator.toString()
    }


    private fun gcd(x: Int, y: Int): Int = if (y == 0) x.abs() else gcd(y, x.rem(y))


    fun Int.abs(): Int {
        return if (this < 0) -this else this;
    }


    // CONSTANTS

    companion object {
        val zero = Fraction(0);
        val one = Fraction(1)
    }
}