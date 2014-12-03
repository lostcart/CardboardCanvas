package carboard.test.ustwo.carboardplayground;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.vrtoolkit.cardboard.CardboardActivity;

/**
 * Main activity, extends cardboard activity to handle the magnetic trigger
 *
 * @author luke@ustwo.com
 */
public class MainActivity extends CardboardActivity {

    private TestView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.main);
        mTestView = (TestView) findViewById(R.id.main_test_view);
    }

    @Override
    public void onCardboardTrigger() {
        mTestView.toggleScrolling();
    }
}
