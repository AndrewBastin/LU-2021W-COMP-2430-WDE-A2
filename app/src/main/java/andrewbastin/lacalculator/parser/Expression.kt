package andrewbastin.lacalculator.parser

import java.util.*

// Converts a list of tokens from (presumed) infix notation to postfix notation
// Using the standard infix to postfix algorithm
private fun convertToPostfix(tokens: List<Token>): List<Token> {
    val result = mutableListOf<Token>()

    val tokenStack = Stack<Token>()

    for (token in tokens) {
        when (token) {
            is NumberToken -> result.add(token)

            is BinaryOpToken -> {
                // Operator precedence
                if (tokenStack.isNotEmpty()) {
                    val preToken = tokenStack.peek()
                    if (preToken is BinaryOpToken) {
                        if (token < preToken) {
                            result.add(tokenStack.pop())
                            tokenStack.add(token)
                        } else {
                            tokenStack.add(token)
                        }
                    }
                } else {
                    tokenStack.push(token)
                }
            }
        }
    }

    // Pop all remaining tokens
    while (tokenStack.isNotEmpty()) result.add(tokenStack.pop())

    return result
}

// An expression is something that can be evaluated to resolve into a value
abstract class Expression {

    companion object {

        fun createExpression(tokens: List<Token>, stack: Stack<Expression> = Stack()): Expression {

            val orderedTokens = convertToPostfix(tokens)
            val expStack = stack

            val iterator = orderedTokens.iterator()

            while (iterator.hasNext()) {
                val token = iterator.next()

                when (token) {

                    is NumberToken -> {
                        expStack.push(LiteralExpression(token))
                    }

                    is BinaryOpToken -> {
                        val right = expStack.pop()
                        val left = expStack.pop()
                        expStack.push(BinaryOpExpression(left, right, token))
                    }

                }

            }

            if (expStack.empty()) throw Exception("[Expression/parser] Empty expression stack")
            if (expStack.size > 1) throw Exception("[Expression/parser] Incomplete expression")

            return expStack.pop()
        }

    }

    abstract fun evaluate(): Float
}

// An expression which just encapsulates a literal value
class LiteralExpression(val literalToken: NumberToken) : Expression() {
    override fun evaluate() = literalToken.value
}

// An expression which has operates a binary operator encapsulating the left and right side of the binary operation
class BinaryOpExpression(val left: Expression, val right: Expression, val opToken: BinaryOpToken): Expression() {
    override fun evaluate() = opToken.op.operator.evaluate(left, right)
}