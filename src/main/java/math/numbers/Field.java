package math.numbers;

public interface Field<T> {

    T addZero();
    T mulZero();

    T add(T a, T b);
    T multiply(T a, T b);

    T addInverse(T a);
    T mulInverse(T a) throws ArithmeticException;

    default T minus(T a, T b) {
        return add(a, addInverse(b));
    }

    default T div(T a, T b) {
        return multiply(a, mulInverse(b));
    }

}
