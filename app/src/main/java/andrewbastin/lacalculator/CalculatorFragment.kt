package andrewbastin.lacalculator

import andrewbastin.lacalculator.parser.Expression
import andrewbastin.lacalculator.parser.Lexer
import andrewbastin.lacalculator.utils.hasFractional
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.IdRes

// A really nifty extension function to Fragment class which we will use to
// lazily perform findViewById with the help of Kotlin Property Delegation
fun <T : View> Fragment.bind(@IdRes res: Int) = lazy { view!!.findViewById(res) as T }

class CalculatorFragment : Fragment() {

    private val calcText: EditText by bind(R.id.calcText)

    private val button0: Button by bind(R.id.button0)
    private val button1: Button by bind(R.id.button1)
    private val button2: Button by bind(R.id.button2)
    private val button3: Button by bind(R.id.button3)
    private val button4: Button by bind(R.id.button4)
    private val button5: Button by bind(R.id.button5)
    private val button6: Button by bind(R.id.button6)
    private val button7: Button by bind(R.id.button7)
    private val button8: Button by bind(R.id.button8)
    private val button9: Button by bind(R.id.button9)

    private val buttonDel: Button by bind(R.id.buttonDel)
    private val buttonAC: Button by bind(R.id.buttonAC)
    private val buttonEquals: Button by bind(R.id.buttonEquals)

    private val buttonPlus: Button by bind(R.id.buttonPlus)
    private val buttonMinus: Button by bind(R.id.buttonMinus)
    private val buttonMul: Button by bind(R.id.buttonMul)
    private val buttonDiv: Button by bind(R.id.buttonDiv)
    private val buttonDot: Button by bind(R.id.buttonDot)
    private val buttonPow: Button by bind(R.id.buttonPow)

    private val calcContainer: LinearLayout by bind(R.id.calcContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()


        // Don't show keyboard on selection of the calcText
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            calcText.showSoftInputOnFocus = false
        } else {
            calcText.setTextIsSelectable(true)
        }

        // Wiring all the number buttons onClickListener
        // Since the logic is pretty similar and being DRY is neato
        arrayOf(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9)
            .forEachIndexed { index, button ->
                button.setOnClickListener {
                    calcText.text.insert(calcText.selectionEnd, index.toString())
                }
            }

        buttonAC.setOnClickListener {
            calcText.text.clear()
        }

        buttonDel.setOnClickListener {
            try {
                calcText.text.delete(calcText.selectionEnd - 1, calcText.selectionEnd)
            } catch (e: Exception) {} // What if there is nothing to delete
        }

        // Fancy shmancy animation setup
        val slideOutBottomAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_top)
        val slideInTopAnim = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)

        slideOutBottomAnim.fillAfter = true


        buttonEquals.setOnClickListener {
            try {
                var count = Expression.createExpression(Lexer.lex(calcText.text.toString())).evaluate()

                slideOutBottomAnim.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                        // If there is now fractional value, remove that bit
                        calcText.setText(
                            if (count.hasFractional()) count.toInt().toString() else count.toString()
                        )
                        calcText.setSelection(calcText.text.length)

                        calcContainer.clearAnimation()

                        calcContainer.startAnimation(slideInTopAnim)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })

                calcContainer.startAnimation(slideOutBottomAnim)

            } catch (e: Exception) {
                // If the expression wasn't parsed properly. Show a toast
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        // Map the operator keys to onClickListeners
        mapOf(
            "+" to buttonPlus,
            "-" to buttonMinus,
            "÷" to buttonDiv,
            "✕" to buttonMul,
            "." to buttonDot,
            "^" to buttonPow
        ).forEach { entry ->
            entry.value.setOnClickListener {
                calcText.text.insert(calcText.selectionEnd, entry.key)
            }
        }

        calcText.requestFocus()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }
}