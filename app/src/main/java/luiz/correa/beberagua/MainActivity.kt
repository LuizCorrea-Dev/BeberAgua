package luiz.correa.beberagua

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import luiz.correa.beberagua.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var hour = 0
    private var minute = 0
    private var interval = 0
    private var activated = false

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.timePicker.setIs24HourView(true)
        preferences = getSharedPreferences("dp", MODE_PRIVATE)


        // buscando dados do db
        activated = preferences.getBoolean("activated", false)

        if (activated) {
            binding.btnNotify.setText(R.string.pause)
            val color = ContextCompat.getColor(this, android.R.color.black)
            binding.btnNotify.setBackgroundColor(color)
            activated = true

            // buscando as propiedades dos campos de texto
            val interval = preferences.getInt("interval", 0)



            val hour = preferences.getInt("hour", binding.timePicker.hour)
            val minute = preferences.getInt("minute", binding.timePicker.minute)
            binding.editMinutes.setText(interval.toString())
            binding.timePicker.hour = hour
            binding.timePicker.minute = minute
        }

        getClickButton()
    }

    private fun getClickButton() {
        binding.btnNotify.setOnClickListener {
            val sInternal = binding.editMinutes.text.toString()
            if (sInternal.isEmpty()) {
                Toast.makeText(this, R.string.error_interval, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            hour = binding.timePicker.hour
            minute = binding.timePicker.minute
            interval = sInternal.toInt()
            if (!activated) {
                binding.btnNotify.setText(R.string.pause)
                val color = ContextCompat.getColor(this, android.R.color.black)
                binding.btnNotify.setBackgroundColor(color)
                activated = true
                val editor = preferences.edit()
                editor.putBoolean("activated", true)
                editor.putInt("hour", hour)
                editor.putInt("minute", minute)
                editor.putInt("interval", interval)
                editor.apply()
            } else {
                binding.btnNotify.setText(R.string.notify)
                val color = ContextCompat.getColor(this, R.color.colorAccent)
                binding.btnNotify.setBackgroundColor(color)
                activated = false
                val editor = preferences.edit()
                editor.putBoolean("activated", false)
                editor.remove("hour")
                editor.remove("minute")
                editor.remove("interval")
                editor.apply()
            }
            Log.i("TEste", "hora" + hour + "minuto" + minute + "intervalo" + interval)
        }
    }



}