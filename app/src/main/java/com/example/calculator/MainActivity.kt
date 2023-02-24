package com.example.calculator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val operators = listOf<String>("+", "-", "×", "÷", ".")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.equalTo.setOnClickListener { equalTo() }
        binding.allClear.setOnClickListener { allClear() }
        binding.backSpace.setOnClickListener { backSpace() }
        binding.period.setOnClickListener { addToInputText(".") }
        binding.plus.setOnClickListener { addToInputText("+") }
        binding.multiply.setOnClickListener { addToInputText("\u00D7") }
        binding.divide.setOnClickListener { addToInputText("\u00f7") }
        binding.subtract.setOnClickListener { addToInputText("-") }
        binding.number0.setOnClickListener { addToInputText("0") }
        binding.number1.setOnClickListener { addToInputText("1") }
        binding.number2.setOnClickListener { addToInputText("2") }
        binding.number3.setOnClickListener { addToInputText("3") }
        binding.number4.setOnClickListener { addToInputText("4") }
        binding.number5.setOnClickListener { addToInputText("5") }
        binding.number6.setOnClickListener { addToInputText("6") }
        binding.number7.setOnClickListener { addToInputText("7") }
        binding.number8.setOnClickListener { addToInputText("8") }
        binding.number9.setOnClickListener { addToInputText("9") }
    }

    fun addToInputText(buttonValue: String) {
        val presentOperator = buttonValue

        //to check whether first thing enetered is an operator
        if(binding.workingTV.text.isBlank() && operators.contains(presentOperator))
            Toast.makeText(this, "Enter a Digit first", Toast.LENGTH_SHORT).show()

        //to check whether 2 consecutive operators are entered
        else if (operators.contains(presentOperator) && binding.workingTV.text.isNotBlank() && operators.contains(
                binding.workingTV.text.last().toString()
            )
        )
            Toast.makeText(this, "Enter a digit", Toast.LENGTH_SHORT).show()
        else binding.workingTV.append(buttonValue)
    }

    fun equalTo() {
        if(operators.any() {binding.workingTV.text.toString().contains(it)})
            binding.resultsTV.text = calculateResults()
        else
            binding.resultsTV.text = binding.workingTV.text
    }

    private fun calculateResults(): String {
        var numbersOperator = numbersOperators()
        if (numbersOperator.isEmpty()) return ""

        val intoDivision = intoDivisionCalculate(numbersOperator)
        val addSubtract = addSubtractCalculate(intoDivision)
        return addSubtract.toString()
    }

    private fun addSubtractCalculate(intoDivision: MutableList<Any>): Float {
        var result = intoDivision[0] as Float

        for (i in intoDivision.indices) {
            if (intoDivision[i] is Char && i != intoDivision.lastIndex) {
                val operator = intoDivision[i]
                val nextDigit = intoDivision[i + 1].toString().toFloat()

                when (operator) {
                    '+' -> result += nextDigit

                    '-' -> result -= nextDigit
                }
            }
        }
        return result
    }

    private fun intoDivisionCalculate(numbersOperator: MutableList<Any>): MutableList<Any> {
        var numbersOperatorList = numbersOperator
        var intoDivisionList = mutableListOf<Any>()

        while (numbersOperatorList.contains('\u00D7') || numbersOperatorList.contains('\u00f7')) {
            var restart = numbersOperatorList.size

            for (i in numbersOperatorList.indices) {
                if (numbersOperatorList[i] is Char && i != numbersOperatorList.lastIndex && i < restart) {
                    val operator = numbersOperatorList[i]
                    val prevdigit = numbersOperatorList[i - 1].toString().toFloat()
                    val nextDigit = numbersOperatorList[i + 1].toString().toFloat()

                    when (operator) {
                        '\u00D7' -> {
                            intoDivisionList.add(prevdigit * nextDigit)
                            restart = i + 1
                        }

                        '\u00f7' -> {
                            intoDivisionList.add(prevdigit / nextDigit)
                            restart = i + 1
                        }

                        else -> {
                            intoDivisionList.add(prevdigit)
                            intoDivisionList.add(operator)
                        }
                    }
                }
                if (i > restart) {
                    intoDivisionList.add(numbersOperatorList[i])
                }
            }
            numbersOperatorList = intoDivisionList.toMutableList()
            intoDivisionList.clear()
        }
        return numbersOperatorList
    }

    private fun numbersOperators(): MutableList<Any> {
        var numbersOperatorsList = mutableListOf<Any>()
        var currentDigit = ""

        for (character in binding.workingTV.text.toString()) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                numbersOperatorsList.add(currentDigit.toFloat())
                currentDigit = ""
                numbersOperatorsList.add(character)
            }
        }
        if (currentDigit != "") numbersOperatorsList.add(currentDigit)
        if (numbersOperatorsList.last() is Char)
            numbersOperatorsList.removeAt(numbersOperatorsList.size - 1)
        return numbersOperatorsList
    }

    fun allClear() {
        binding.workingTV.text = ""
        binding.resultsTV.text = ""
    }

    fun backSpace() {
        var stringInWorkingTV = binding.workingTV.text

        if (binding.workingTV.text.isNotBlank()) {
            binding.workingTV.text = stringInWorkingTV.substring(0, stringInWorkingTV.length - 1)
        }
    }
}