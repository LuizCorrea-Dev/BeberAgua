package luiz.correa.beberagua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import luiz.correa.beberagua.databinding.ActivityMainJavaBinding;

public class MainActivityJava extends AppCompatActivity {

    private ActivityMainJavaBinding binding;
    private int hour;
    private int minute;
    private int interval;
    private Boolean activated = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainJavaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.timePicker.setIs24HourView(true);
        preferences = getSharedPreferences("dp2", Context.MODE_PRIVATE);

        // buscando dados do db
        activated = preferences.getBoolean("activated",false);

        if(activated) {
            binding.btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.black);
            binding.btnNotify.setBackgroundColor(color);
            activated = true;

            // buscando as propiedades dos campos de texto
            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", binding.timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", binding.timePicker.getCurrentMinute());

            binding.editMinutes.setText(String.valueOf(interval));
            binding.timePicker.setCurrentHour(hour);
            binding.timePicker.setCurrentMinute(minute);

        }

        getClickButton();


    }

    private void getClickButton() {
        binding.btnNotify.setOnClickListener(view -> {

            String sInternal = binding.editMinutes.getText().toString();

            if(sInternal.isEmpty()) {
                Toast.makeText(this, R.string.error_interval, Toast.LENGTH_SHORT).show();
                return;
            }

            hour = binding.timePicker.getCurrentHour();
            minute = binding.timePicker.getCurrentMinute();
            interval = Integer.parseInt(sInternal);

            if(!activated) {
                binding.btnNotify.setText(R.string.pause);
                int color = ContextCompat.getColor(this, android.R.color.black);
                binding.btnNotify.setBackgroundColor(color);
                activated = true;


                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activated", true);
                editor.putInt("hour", hour);
                editor.putInt("minute", minute);
                editor.putInt("interval", interval);
                editor.apply();

            } else {
                binding.btnNotify.setText(R.string.notify);
                int color = ContextCompat.getColor(this, R.color.colorAccent);
                binding.btnNotify.setBackgroundColor(color);
                activated = false;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activated", false);
                editor.remove("hour");
                editor.remove("minute");
                editor.remove("interval");
                editor.apply();
            }

            Log.i("TEste", "hora" + hour + "minuto" + minute + "intervalo" + interval);

        });


    }


}