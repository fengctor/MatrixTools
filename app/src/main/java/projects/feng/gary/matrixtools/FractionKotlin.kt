package projects.feng.gary.matrixtools


class FractionKotlin {
    private val numerator: Int
    private val denominator: Int

    constructor(numer: Int, denom: Int) {
        val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    constructor(stringRep: String) {
        val fracParts = stringRep.split("/".toRegex())

        val numer = fracParts.get(0).toInt()
        val denom = if (fracParts.size == 1) 1 else fracParts.get(1).toInt()

        val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    operator fun plus(other: FractionKotlin): FractionKotlin {
        val newNumer = this.numerator * other.denominator + other.numerator * this.denominator
        val newDenom = this.denominator * other.denominator

        return FractionKotlin(newNumer, newDenom)
    }

    operator fun minus(other: FractionKotlin): FractionKotlin {
        return this + -other
    }

    operator fun times(other: FractionKotlin): FractionKotlin {
        val newNumer = this.numerator * other.numerator
        val newDenom = this.denominator * other.denominator

        return FractionKotlin(newNumer, newDenom)
    }

    operator fun div(other: FractionKotlin): FractionKotlin {
        return this * other.reciprocal()
    }

    operator fun unaryMinus(): FractionKotlin {
        return FractionKotlin(-this.numerator, this.denominator)
    }

    fun reciprocal(): FractionKotlin {
        return FractionKotlin(this.denominator, this.numerator)
    }


    override fun equals(other: Any?): Boolean {
        return other is FractionKotlin
                && other.numerator == this.numerator
                && other.denominator == this.denominator
    }

    override fun toString(): String {
        return if (this.denominator == 0)
            this.numerator.toString()
        else
            this.numerator.toString() + "/" + this.denominator.toString()
    }


    private fun gcd(x: Int, y: Int): Int = if (y == 0) x.abs() else gcd(y, x.rem(y))


    fun Int.abs(): Int {
        return if (this < 0) -this else this;
    }
}