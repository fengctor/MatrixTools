package projects.feng.gary.matrixtools


class Fraction {
    private val numerator: Int
    private val denominator: Int

    companion object {
        val zero = Fraction(0);
        val one = Fraction(1)
    }


    //---------------------------CONSTRUCTORS-----------------------------------------------------//

    constructor(numer: Int, denom: Int) {
        val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    constructor(integer: Int) {
        numerator = integer
        denominator = 1
    }

    constructor(str: String) {
        val stringRep = str.replace("\\s".toRegex(), "")

        when {
            stringRep.contains('/') -> {
                val fracParts = stringRep.split("/".toRegex())

                val numer = fracParts.get(0).toInt()
                val denom = fracParts.get(1).toInt()

                val gcd = if (denom < 0) -gcd(numer, denom) else gcd(numer, denom)

                numerator = numer / gcd
                denominator = denom / gcd
            }
            stringRep.contains('.') -> {
                val decParts = stringRep.split("\\.".toRegex())
                val numDecPoints = decParts[1].length

                // reduce fractional component before adding on the integer component
                var fracPartNum = decParts[1].toInt()
                var fracPartDenom = powerOfTen(numDecPoints)

                val gcd = gcd(fracPartNum, fracPartDenom)
                fracPartNum /= gcd
                fracPartDenom /= gcd

                val intPart = decParts[0].toInt() * fracPartDenom

                numerator = (fracPartNum + intPart) *
                        if (decParts[0].toInt() == 0 && decParts[0].contains('-')) -1 else 1
                denominator = fracPartDenom
            }
            else -> {
                numerator = stringRep.toInt()
                denominator = 1
            }
        }
    }


    //---------------------------ARITHMETIC FUNCTIONS---------------------------------------------//

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


    //---------------------------OVERRIDES--------------------------------------------------------//

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


    //---------------------------HELPERS----------------------------------------------------------//

    private fun gcd(x: Int, y: Int): Int = if (y == 0) x.abs() else gcd(y, x.rem(y))

    private fun powerOfTen(n: Int): Int {
        var result = 1

        for (i in 1..n) {
            result *= 10
        }

        return result
    }
}