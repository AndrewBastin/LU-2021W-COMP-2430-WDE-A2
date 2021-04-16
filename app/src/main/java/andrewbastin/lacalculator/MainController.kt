package andrewbastin.lacalculator

import andrewbastin.lacalculator.parser.Expression
import andrewbastin.lacalculator.parser.Lexer

object MainController {

    fun performCalculation(input: String): Float =
        Expression.createExpression(Lexer.lex(input)).evaluate()

}