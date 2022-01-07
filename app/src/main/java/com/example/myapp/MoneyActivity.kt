package com.example.myapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*



class MoneyActivity : AppCompatActivity() {
    private lateinit var editTextMoney: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var textViewMoneyADay: TextView
    private lateinit var textViewMoneyAMonth: TextView
    private lateinit var button: Button
    private lateinit var seekBar: SeekBar
    private lateinit var seekBar2: SeekBar
    private lateinit var textViewSeekBarProgress: TextView
    private lateinit var textViewPref: TextView
    private lateinit var textViewSeekBar2: TextView
    private lateinit var editTextMoneyWished: EditText
    private lateinit var textViewMoneyAvailable : TextView

    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money)

        init()

        seekBarsInit()

        //Подгрузка сохраненного состояния
        textViewPref.text = pref?.getInt("money", 0).toString()
        editTextMoney.setText(pref?.getInt("money", 0).toString())
        editTextMoneyWished.setText(pref?.getInt("moneyWished", 0).toString())

        //Подгрузка сохраненного состояния checkBox при запуске
        var stateOfCheckBox = pref?.getBoolean("checkBox", false)
        if (checkBox.isChecked != stateOfCheckBox) checkBox.toggle()
    }

    private fun init() {
        editTextMoney = findViewById(R.id.editTextMoney)
        checkBox = findViewById(R.id.checkBox)
        textViewMoneyADay = findViewById(R.id.textViewMoneyADay)
        textViewMoneyAMonth = findViewById(R.id.textViewMoneyAMonth)
        button = findViewById(R.id.button)
        seekBar = findViewById(R.id.seekBar)
        seekBar2 = findViewById(R.id.seekBar2)
        textViewSeekBarProgress = findViewById(R.id.textViewSeekBarProgress)
        textViewPref = findViewById(R.id.textViewPref)
        textViewSeekBar2 = findViewById(R.id.textViewSeekBar2)
        editTextMoneyWished = findViewById(R.id.editTextMoneyWished)
        textViewMoneyAvailable = findViewById(R.id.textViewMoneyAvailable)


        pref = getSharedPreferences("Name", Context.MODE_PRIVATE)
    }

    private fun seekBarsInit() {
        seekBar.min = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)                 // Минимальное значение seekBar (дней с начала месяца)
        //seekBar.max = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)  // Максимальное значение seekBat (дней в месяце)
        seekBar.max = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + 5           // Максимальное значение seekBat текущий день плюс 5 дней
        seekBar.progress = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)            // Прогресс seekBar (дней с начала месяца)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewSeekBarProgress.text = "$progress"
                countMoney(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekBar2.min = 0
        seekBar2.max = 2000
        seekBar2.progress = 0
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewSeekBar2.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun countMoney(todayDay: Int) {
        val rentPay = 21000 // Аренда квартиры

        //Получение и приведение значения в поле к типу Int
        val money = (editTextMoney.text).toString().toInt()

        // Количество дней в месяце
        val countDayMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

        //Денег в день
        val moneyOfDay = if (checkBox.isChecked) {  // Если квартплата уже внесена
            (money - 21000) / todayDay              // то вычитаем ее из суммы
        } else {
            money / todayDay                        // Если нет, то просто делим на количество дней
        }
        textViewMoneyADay.text = "Денег в день: $moneyOfDay"    // Выводим в textView

        // Количество денег в месяц с учетом квартплаты
        val moneyMonth = (moneyOfDay * countDayMonth) + rentPay   // Количество денег в месяц вместе с квартплатой
        textViewMoneyAMonth.text = "Денег в месяц: $moneyMonth"

        // Подсчет и вывод количества денег, которые можно потратить сегодня
        //Получение и приведение значения в поле к типу Int
        val moneyWished = (editTextMoneyWished.text).toString().toInt() // Желаемая сумма в месяц

        if(moneyWished > 0) {
            val moneyAvailable = (moneyWished - rentPay) / 31 * todayDay - money //Сколько можно потратить сегодня
            textViewMoneyAvailable.text = "Можно потратить: $moneyAvailable"
        }


    }

    private fun saveMoneyPref(money: Int, checkBox: Boolean, moneyWished: Int) {
        val editor = pref?.edit()
        editor?.putInt("money", money)
        editor?.putBoolean("checkBox", checkBox)
        editor?.putInt("moneyWished", moneyWished)
        editor?.apply()
    }

    override fun onStop() {
        super.onStop()
        saveMoneyPref(editTextMoney.text.toString().toInt(), checkBox.isChecked, editTextMoneyWished.text.toString().toInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        saveMoneyPref(editTextMoney.text.toString().toInt(), checkBox.isChecked, editTextMoneyWished.text.toString().toInt())
    }
}