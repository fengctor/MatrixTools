package projects.feng.gary.matrixtools

import java.math.BigInteger


class Fraction {
    private val numerator: BigInteger
    private val denominator: BigInteger

    companion object {
        val zero = Fraction(0);
        val one = Fraction(1)
    }


    //---------------------------CONSTRUCTORS-----------------------------------------------------//

    constructor(numer: BigInteger, denom: BigInteger) {
        val gcd = if (denom < 0.toBigInteger()) -gcd(numer, denom) else gcd(numer, denom)

        numerator = numer / gcd
        denominator = denom / gcd
    }

    constructor(integer: Int) {
        numerator = integer.toBigInteger()
        denominator = 1.toBigInteger()
    }

    constructor(str: String) {
        val stringRep = str.replace("\\s".toRegex(), "")

        when {
            stringRep.equals("") -> {
                numerator = 0.toBigInteger()
                denominator = 1.toBigInteger()
            }
            stringRep.contains('/') -> {
                val fracParts = stringRep.split("/".toRegex())

                val numer = fracParts.get(0).toBigInteger()
                val denom = fracParts.get(1).toBigInteger()

                val gcd = if (denom < 0.toBigInteger()) -gcd(numer, denom) else gcd(numer, denom)

                numerator = numer / gcd
                denominator = denom / gcd
            }
            stringRep.contains('.') -> {
                val decParts = stringRep.split("\\.".toRegex())
                val numDecPoints = decParts[1].length

                // reduce fractional component before adding on the integer component
                var fracPartNum = if (numDecPoints == 0) 0.toBigInteger() else decParts[1].toBigInteger()
                var fracPartDenom = powerOfTen(numDecPoints)

                val gcd = gcd(fracPartNum, fracPartDenom)
                fracPartNum /= gcd
                fracPartDenom /= gcd

                val intPart = decParts[0].toBigInteger() * fracPartDenom

                numerator = (fracPartNum + intPart) *
                        if (decParts[0].toInt() == 0 && decParts[0].contains('-')) -1.toBigInteger() else 1.toBigInteger()
                denominator = fracPartDenom
            }
            else -> {
                numerator = stringRep.toBigInteger()
                denominator = 1.toBigInteger()
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
        return if (this.denominator == 1.toBigInteger())
            this.numerator.toString()
        else
            this.numerator.toString() + "/" + this.denominator.toString()
    }


    //---------------------------HELPERS----------------------------------------------------------//

    private fun gcd(x: BigInteger, y: BigInteger): BigInteger = if (y == 0.toBigInteger()) x.abs() else gcd(y, x.rem(y))

    private fun powerOfTen(n: Int): BigInteger {
        var result = 1

        for (i in 1..n) {
            result *= 10
        }

        return result.toBigInteger()
    }
}