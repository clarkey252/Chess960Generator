package uk.co.clarkey252.chess960generator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final char KING = '♔';
    public static final char QUEEN = '♕';
    public static final char ROOK = '♖';
    public static final char BISHOP = '♗';
    public static final char KNIGHT = '♘';

    public static final char NULL_CHAR = '\u0000';

    private TextView mOutputText;
    private EditText mIdText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mOutputText = findViewById(R.id.text_output);
        mIdText = findViewById(R.id.editText_id);
        mIdText.addTextChangedListener(inputWatcher);
        findViewById(R.id.button_random).setOnClickListener(randomClick);
        updateRandom();
    }

    TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int input = Integer.valueOf(mIdText.getText().toString());
                if (input >= 0 && input <= 959) {
                    mIdText.setBackgroundColor(getResources().getColor(R.color.input_background));
                    update(input);
                    return;
                }
            } catch (NumberFormatException ex) {
                Log.e("InputError", "Input format error", ex);
            }
            if (!mIdText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.input_bound_error, Toast.LENGTH_SHORT).show();
            }
            mIdText.setBackgroundColor(Color.RED);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    View.OnClickListener randomClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            updateRandom();
        }
    };

    private void updateRandom() {
        int id = (int) (Math.random() * 960f);
        mIdText.setText(String.valueOf(id));
        update(id);
    }

    private void update(int id) {
        //0=a...7=h
        char[] output = new char[8];

        //Light Bishop at b,d,f or h
        int b1 = 2 * (id % 4) + 1;
        output[b1] = BISHOP;
        id /= 4;

        //Dark bishop at a,c,e or g
        int b2 = 2 * (id % 4);
        output[b2] = BISHOP;
        id /= 4;

        //Queen at remaining spaces (6)
        int q = id % 6;
        for (int i = 0; i <= q; i++) {
            if (output[i] != NULL_CHAR) {
                q++;
            }
        }
        output[q] = QUEEN;
        id /= 6;

        //Knights on the remaining 5 spaces (5!/2!3! = 10, 2 Knights 3 Spaces)
        int[] knightPos = getKnightPos(id);
        //use a cursor (pos) for the spaces)
        int pos = -1;
        for (int i = 0; i < 8; i++) {
            if (output[i] == NULL_CHAR) {
                pos++;
                if (knightPos[0] == pos || knightPos[1] == pos) {
                    output[i] = KNIGHT;
                }
            }
        }

        //Reset the cursor and put Rook, King, Rook
        pos = -1;
        for (int i = 0; i < 8; i++) {
            if (output[i] == NULL_CHAR) {
                pos++;
                if (pos % 2 == 0) {
                    output[i] = ROOK;
                } else {
                    output[i] = KING;
                }
            }
        }
        mOutputText.setText(new String(output));
    }

    private int[] getKnightPos(int id) {
        int[] output = new int[2];
        if (id < 4) {
            output[0] = 0;
            output[1] = id + 1;
        } else if (id < 7) {
            output[0] = 1;
            output[1] = id - 2;
        } else if (id < 9) {
            output[0] = 2;
            output[1] = id - 4;
        } else if (id == 9) {
            output[0] = 3;
            output[1] = 4;
        } else throw new IllegalArgumentException("Knight pos takes argument 0-9");
        return output;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menu_about) {
            AboutDialogFragment about = new AboutDialogFragment();
            about.show(getSupportFragmentManager(),"AboutFragment");
        }
        if (id == R.id.menu_donate) {
            DonateDialogFragment ddf = new DonateDialogFragment();
            ddf.show(getSupportFragmentManager(),"DonateFragment");
        }
        return super.onOptionsItemSelected(item);
    }
}
