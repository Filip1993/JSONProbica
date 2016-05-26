package com.filipkesteli.jsonprobica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etMovieName;
    private Button btnGetMovie;
    private Callback<Movie> callback;
    private IMovies iMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        setupListeners();

        setupRestAdapters(); //postavljamo RETROFITOV REST Adapter
    }

    private void initWidgets() {
        etMovieName = (EditText) findViewById(R.id.etMovieName);
        btnGetMovie = (Button) findViewById(R.id.btnGetMovie);
    }

    private void setupListeners() {
        btnGetMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //moramo settirati REST adapter
                //moramo ga trznuti
                //moramo se zakaciti na WEB SERVIS (Kljucni moment)
                if (formIsOK()) {
                    //Ako postoji, uzimamo film koji se zove kao uneseni
                    iMovies.getMovie(etMovieName.getText().toString(), callback);
                }
            }
        });
    }

    private boolean formIsOK() {
        if (etMovieName.getText().toString().trim().length() == 0) {
            Toast.makeText(MainActivity.this, R.string.enter_title, Toast.LENGTH_SHORT).show();
            etMovieName.requestFocus();
            return false; //znaci da nismo nista unijeli
        }
        return true;
    }

    private void setupRestAdapters() {
        //REST Adapter zna da ce se spojiti na www.tralala.com/itd...
        //Buildamo novi RestAdapter sa endpointom (tamo RestAdapter gleda) definiranim s konstantom u Interfaceu IMovies
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(IMovies.ENDPOINT_URL)
                .build();

        iMovies = restAdapter.create(IMovies.class);
        //moramo napraviti CALLBACK:
        //proparsiran JSON objekt Movie s interneta, sad mozemo napisati sto hocemo:
        callback = new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(movie.getTitle() + "/n");
                stringBuilder.append(movie.getYear() + "/n");
                stringBuilder.append(movie.getDirector() + "/n");
                stringBuilder.append(movie.getActors() + "/n");
                String text = stringBuilder.toString();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
