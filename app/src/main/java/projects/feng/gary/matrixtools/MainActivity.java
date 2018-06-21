package projects.feng.gary.matrixtools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.rrefButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView text = findViewById(R.id.text);

                Fraction[] test = {new Fraction(5), new Fraction(3), new Fraction(2),
                                   new Fraction("3/2"), new Fraction(0), new Fraction("1/2"),
                                   new Fraction(3), new Fraction(0), new Fraction(1),
                                   new Fraction(6), new Fraction(0), new Fraction(2)};
                Matrix matrix = new Matrix(test, 4, 3);
                Fraction[] rref = matrix.RREF();

                for (int i = 0; i < 4; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        text.append(rref[matrix.getIndex(i, j)] + "\t\t");
                    }
                    text.append("\n");
                }
            }
        });
    }


}
