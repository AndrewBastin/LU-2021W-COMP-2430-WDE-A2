package andrewbastin.lacalculator.parser

// Lexer takes an input string and spits out a set of tokens
object Lexer {

    // Array of token emitters
    // The order is with which matching is checked, so should be sorted according to the priority
    private val emitters = arrayOf(
            // Order Matters!!!
            BinaryOpToken.Emitter,
            NumberToken.Emitter
    )

    fun lex(input: String): List<Token> {

        var working = input
        val tokens = mutableListOf<Token>()

        while (working.isNotEmpty()) {
            // Find an emitter which can emit a token with the working string
            val emitter = emitters.find { it.canEmit(working) }

            // If such an emitter is found, emit the token and cut down the working string
            // Else throw exception because this is an invalid input
            if (emitter != null) {
                val (newWorking, token) = emitter.emitToken(working)
                tokens.add(token)
                working = newWorking
            } else throw Exception("[lex] Failed lexing ($working)")
        }

        return tokens
    }

}