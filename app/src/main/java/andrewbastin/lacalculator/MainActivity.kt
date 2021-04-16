package andrewbastin.lacalculator

import andrewbastin.lacalculator.parser.Expression
import andrewbastin.lacalculator.parser.Lexer
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

// Another nifty extension function on Float to check if a float has a fractional part
// Example: (4.5).hasFractional() -> true
//          (4.0).hasFractional() -> false
fun Float.hasFractional() = this.toInt().toFloat() != this

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}