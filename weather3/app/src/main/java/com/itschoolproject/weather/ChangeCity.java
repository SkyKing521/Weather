package com.itschoolproject.weather;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ChangeCity extends AppCompatActivity implements View.OnClickListener {

    Button getcity, Moscow, SaintPetersburg, Novosibirsk, Ekaterinburg, Kazan, NizhniyNovgorod, Chelyabinsk, Omsk,
            Samara, RostovNaDonu, Ufa, Krasnoyarsk, Perm, Voronezh, Volgograd;

    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_activity);

        getcity = findViewById(R.id.getcity);
        Moscow = findViewById(R.id.Moscow);
        SaintPetersburg = findViewById(R.id.SaintPetersburg);
        Novosibirsk = findViewById(R.id.Novosibirsk);
        Ekaterinburg = findViewById(R.id.Ekaterinburg);
        Kazan = findViewById(R.id.Kazan);
        NizhniyNovgorod = findViewById(R.id.NizhniyNovgorod);
        Chelyabinsk = findViewById(R.id.Chelyabinsk);
        Omsk = findViewById(R.id.Omsk);
        Samara = findViewById(R.id.Samara);
        RostovNaDonu = findViewById(R.id.RostovNaDonu);
        Ufa = findViewById(R.id.Ufa);
        Krasnoyarsk = findViewById(R.id.Krasnoyarsk);
        Perm = findViewById(R.id.Perm);
        Voronezh = findViewById(R.id.Voronezh);
        Volgograd = findViewById(R.id.Volgograd);
        city = findViewById(R.id.city);
        getcity.setOnClickListener(this);
        Moscow.setOnClickListener(this);
        SaintPetersburg.setOnClickListener(this);
        Novosibirsk.setOnClickListener(this);
        Ekaterinburg.setOnClickListener(this);
        Kazan.setOnClickListener(this);
        NizhniyNovgorod.setOnClickListener(this);
        Chelyabinsk.setOnClickListener(this);
        Omsk.setOnClickListener(this);
        Samara.setOnClickListener(this);
        RostovNaDonu.setOnClickListener(this);
        Ufa.setOnClickListener(this);
        Krasnoyarsk.setOnClickListener(this);
        Perm.setOnClickListener(this);
        Voronezh.setOnClickListener(this);
        Volgograd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case R.id.getcity: intent.putExtra("city", city.getText().toString()); break;
            case R.id.Moscow: intent.putExtra("city", "Москва"); break;
            case R.id.SaintPetersburg: intent.putExtra("city", "Санкт-Петербург"); break;
            case R.id.Novosibirsk: intent.putExtra("city", "Новосибирск"); break;
            case R.id.Ekaterinburg: intent.putExtra("city", "Екатеринбург"); break;
            case R.id.Kazan: intent.putExtra("city", "Казань"); break;
            case R.id.NizhniyNovgorod: intent.putExtra("city", "Нижний Новгород"); break;
            case R.id.Chelyabinsk: intent.putExtra("city", "Челябинск"); break;
            case R.id.Omsk: intent.putExtra("city", "Омск"); break;
            case R.id.Samara: intent.putExtra("city", "Самара"); break;
            case R.id.RostovNaDonu: intent.putExtra("city", "Ростов на Дону"); break;
            case R.id.Ufa: intent.putExtra("city", "Уфа"); break;
            case R.id.Krasnoyarsk: intent.putExtra("city", "Красноярск"); break;
            case R.id.Perm: intent.putExtra("city", "Пермь"); break;
            case R.id.Voronezh: intent.putExtra("city", "Воронеж"); break;
            case R.id.Volgograd: intent.putExtra("city", "Волгоград"); break;
        }
        startActivity(intent);
    }
}