package co.edu.uis.conectacampo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MetodoDePago extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_de_pago);
    }

    public void efectivo(View  v)
    {
        Intent comprar = new Intent(MetodoDePago.this,Comprar.class);
        startActivity(comprar);

    }

    public void tarjeta(View  v)
    {
        Intent comprar = new Intent(MetodoDePago.this,Comprar.class);
        startActivity(comprar);

    }
}
