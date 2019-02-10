package pe.hgs.truler.phase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pe.hgs.truler.R;

/** 구현은 보류
 *
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

		findViewById(R.id.button_mm_run).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

	@Override
	public void onClick(View view) {
		setResult(RESULT_OK);
		finish();
	}
}
