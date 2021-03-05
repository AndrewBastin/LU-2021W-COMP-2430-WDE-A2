package andrewbastin.lacalculator.parser

import java.util.regex.Pattern

// Just a simple class all the tokens inherit
abstract class Token

// Ingests expression string and emit tokens
abstract class TokenEmitter<T: Token> {

    abstract val regex: Regex

    private val pattern by lazy { Pattern.compile(regex.toString()) }

    // Check whether the input string can be eaten to emit a token
    fun canEmit(input: String): Boolean {
        val match = regex.find(input)

        return match != null && match.range.first == 0
    }

    // Returns the remaining uneaten lexer string and the emitted token
    fun emitToken(input: String): Pair<String, T> {
        val match = regex.find(input)

        if (match != null) {

            if (match.range.last == input.length - 1) return Pair("", getToken(match.value))
            return Pair(input.substring(match.range.last + 1), getToken(match.value))

        } else throw Exception("Token Emission with a null match $input")

    }

    // Implements how to parse the matched part to the resulting token
    protected abstract fun getToken(match: String): T
}

// A token representing literal number values (both integers and floats)
class NumberToken(val value: Float, val isFloatingPoint: Boolean = false) : Token() {
    companion object Emitter : TokenEmitter<NumberToken>() {
        override val regex = "[-]?[0-9]*\\.?[0-9]+".toRegex()

        override fun getToken(match: String) = NumberToken(match.toFloat(), match.contains("."))
    }
}

enum class BinaryOp(val precedence: Int, val operator: BinaryOperator) {
    Addition(3, AddOperator()),
    Subtraction(3, SubtractOperator()),

    Multiplication(4, MultiplyOperator()),
    Division(4, DivideOperator()),

    Power(5, PowOperator())
}

// Representing a binary operation token
class BinaryOpToken(val op: BinaryOp) : Token(), Comparable<BinaryOpToken> {
    companion object Emitter : TokenEmitter<BinaryOpToken>() {

        override val regex = "([✕÷^+-])".toRegex()

        override fun getToken(match: String) = BinaryOpToken(

                when (match) {

                    "+" -> BinaryOp.Addition
                    "-" -> BinaryOp.Subtraction
                    "÷" -> BinaryOp.Division
                    "✕" -> BinaryOp.Multiplication
                    "^" -> BinaryOp.Power

                    else -> throw Exception("[token/BinaryOpToken] Invalid input($match), make sure input was validated")

                }
        )

    }

    // Implementing a comparison so we can compare tokens based on precedence levels
    override fun compareTo(other: BinaryOpToken): Int = this.op.precedence.compareTo(other.op.precedence)
}
