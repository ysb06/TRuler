package pe.hgs.truler.tools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pe.hgs.truler.R;

public class LogWindow extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_window);

		TextView text = (TextView) findViewById(R.id.text_lw_log);
		text.setText(Logger.getLogs());
	}
}
