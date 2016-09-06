package pe.hgs.truler.phase.legacy.analysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pe.hgs.truler.R;

/** 프로필 입력 단계
 *
 */
public class ProfileSelection extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);
		Button btNext = (Button) findViewById(R.id.btNext);
		btNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
