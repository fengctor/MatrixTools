package projects.feng.gary.matrixtools;

public class Fraction {
    private final int numerator;
    private final int denominator;

    public static Fraction zero = new Fraction(0, 1);
    public static Fraction one = new Fraction(1, 1);

    public Fraction(int numerator, int denominator) {
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(int number) {
        this.numerator = number;
        this.denominator = 1;
    }

    public Fraction(String fractionRepresentation) {
        fractionRepresentation = fractionRepresentation.replaceAll("\\s+", "");
        String[] fractionParts = fractionRepresentation.split("/");

        this.numerator = Integer.parseInt(fractionParts[0]);
        this.denominator = Integer.parseInt(fractionParts[1]);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public Fraction addedBy(Fraction fraction) {
        int commonDenominator = lcm(this.getDenominator(), fraction.getDenominator());

        int firstNumerator = this.getNumerator() * commonDenominator / this.getDenominator();
        int secondNumerator = fraction.getNumerator() * commonDenominator / fraction.getDenominator();

        int newNumerator = firstNumerator + secondNumerator;

        int gcd = gcd(newNumerator, commonDenominator);

        return new Fraction(newNumerator / gcd, commonDenominator / gcd);
    }

    public Fraction subtractedBy(Fraction fraction) {
        return this.addedBy(fraction.negate());
    }

    public Fraction multipliedBy(Fraction fraction) {
        int newNumerator = this.numerator * fraction.numerator;
        int newDenominator = this.denominator * fraction.denominator;

        int gcd = gcd(newNumerator, newDenominator);

        return new Fraction(newNumerator / gcd, newDenominator / gcd);
    }

    public Fraction dividedBy(Fraction fraction) {
        return multipliedBy(fraction.reciprocal());
    }

    public Fraction negate() {
        return new Fraction(-this.getNumerator(), this.getDenominator());
    }

    public Fraction reciprocal() {
        return new Fraction(this.getDenominator(), this.getNumerator());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Fraction)) {
            return false;
        }

        Fraction fraction = (Fraction) obj;
        return this.getNumerator() == fraction.getNumerator()
                && this.getDenominator() == fraction.getDenominator();
    }

    @Override
    public String toString() {
        return denominator == 1 ? String.valueOf(numerator) : "(" + this.getNumerator() + "/" + this.getDenominator() + ")";
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }

        a = a < 0 ? -a : a;
        b = b < 0 ? -b : b;

        return gcd(b, a % b);
    }

    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }
}
