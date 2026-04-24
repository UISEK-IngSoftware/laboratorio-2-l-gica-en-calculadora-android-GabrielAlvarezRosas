package ec.edu.uisek.calculator

import android.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class CalculatorState(
    val display: String ="0"
)
sealed class CalculatorEvent{
    data class Number(val number: String): CalculatorEvent()
    data class Operator(val operator: String): CalculatorEvent()
    object Clear: CalculatorEvent()
    object AllClear: CalculatorEvent()
    object Calculate: CalculatorEvent()
    object Decimal: CalculatorEvent()
}

class CalculatorViewModel: ViewModel(){
    private var number1: String = ""
    private var number2: String = ""
    private var operator: String? = null


    var state by mutableStateOf(value = CalculatorState())
        private set

    fun onEvent(event: CalculatorEvent) {
        when(event){
            is CalculatorEvent.Number -> enterNumber(number =event.number)
            is CalculatorEvent.Operator -> enterOperator(operator =event.operator)
            is CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.AllClear -> clearAll()
            is CalculatorEvent.Clear -> clearLast()
            is CalculatorEvent.Calculate -> performCalculator()
        }
    }
    /*
    Gestionar evento de ingreso de numero
     */
    private fun enterNumber(number: String){
        if(operator==null){
            number1+=number
            state=state.copy(display = number1)
        } else {
            number2+=number
            state=state.copy(display = number2)
        }
    }
    /*
    Gestionar evento de ingreso de decimal
     */
    private fun enterDecimal(){
        val currentNumber = if(operator==null) number1 else number2
        if(!currentNumber.contains(other = ".")){
            if(operator==null) {
                number1+="."
                state=state.copy(display = number1)
            } else{
                number2+="."
                state=state.copy(display = number2)
            }
        }
    }
    /*
    Gestionar evento de ingreso de operadores
     */
    private fun enterOperator(operator: String){
        if (number1.isBlank()) return

        if (number2.isNotBlank()&&this.operator !=null) {
            performCalculator()
        }
        this.operator = operator
    }
    private fun clearAll(){
        number1=""
        number2=""
        operator=null
        state= state.copy(display = "0")
    }

    /*
    Gestionar evento de borrado
     */
    private fun clearLast (){
        if (operator == null){
            number1=number1.dropLast(n=1)
            state= state.copy(display = number1)
        } else {
            number2 =number2.dropLast(n=1)
            state= state.copy(display = number2)
        }
    }
    /*
    Gestionar cálculo dependiendo el operador
     */
    private fun performCalculator(){
        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()
        if(num1 !=null && num2 != null && operator !=null ){
            val result = when (operator){
                "+" -> num1 + num2
                "−" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 !=0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }
            clearAll()
            val resultStr = if (result.isNaN()) "ERROR" else result.toString().removeSuffix(suffix = ".0")
            number1 = if (!result.isNaN()) resultStr else ""
            state=state.copy(display = resultStr)
        }
    }

}