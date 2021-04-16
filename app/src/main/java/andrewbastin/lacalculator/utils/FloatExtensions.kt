package andrewbastin.lacalculator.utils

// Another nifty extension function on Float to check if a float has a fractional part
// Example: (4.5).hasFractional() -> true
//          (4.0).hasFractional() -> false
fun Float.hasFractional() = this.toInt().toFloat() != this