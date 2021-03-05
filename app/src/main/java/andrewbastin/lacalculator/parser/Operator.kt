package andrewbastin.lacalculator.parser

import kotlin.math.pow

abstract class Operator

// An abstract class representing the features of every binary operator
abstract class BinaryOperator : Operator() {
    abstract fun evaluate(left: Expression, right: Expression): Float
}

class AddOperator : BinaryOperator() {
    override fun evaluate(left: Expression, right: Expression) = left.evaluate() + right.evaluate()
}

class SubtractOperator : BinaryOperator() {
    override fun evaluate(left: Expression, right: Expression) = left.evaluate() - right.evaluate()
}

class MultiplyOperator : BinaryOperator() {
    override fun evaluate(left: Expression, right: Expression) = left.evaluate() * right.evaluate()
}

class DivideOperator : BinaryOperator() {
    override fun evaluate(left: Expression, right: Expression) = left.evaluate() / right.evaluate()
}

class PowOperator : BinaryOperator() {
    override fun evaluate(left: Expression, right: Expression) = left.evaluate().toDouble().pow(right.evaluate().toDouble()).toFloat()
}