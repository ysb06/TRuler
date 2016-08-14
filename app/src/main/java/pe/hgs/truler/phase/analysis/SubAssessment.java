package pe.hgs.truler.phase.analysis;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.ImageLoader;
import pe.hgs.truler.tools.Logger;

public class SubAssessment extends AppCompatActivity implements View.OnClickListener {

	public static final int STATE_UPPER = 1;
	public static final int STATE_LOWER = 2;

	private ImageView ivSelected;
	private ImageView ivSample;
	private Button btYes;
	private Button btNo;
	private TextView txtState;

	private ImageLoader imgSelected;
	private Uri uriPath;

	private int iState = 0;
	private boolean isUpper = false;
	private boolean isLower = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_assessment);

        ivSelected = (ImageView) findViewById(R.id.ivSelected);
		ivSample = (ImageView) findViewById(R.id.ivSample);
		btYes = (Button) findViewById(R.id.btYes);
		btNo = (Button) findViewById(R.id.btNo);
		txtState = (TextView) findViewById(R.id.txtState);

		btYes.setOnClickListener(this);
		btNo.setOnClickListener(this);

		uriPath = getIntent().getParcelableExtra("ImageUriPath");
		if(uriPath == null)
			Logger.error("The path is null");
		initScreen();

		iState = STATE_UPPER;
    }

	private void initScreen() {
		imgSelected = new ImageLoader(this.getContentResolver(), uriPath);
		ivSelected.setImageBitmap(imgSelected.getImage());
	}

	@Override
	protected void onDestroy() {
		imgSelected.dispose();
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ivSelected.setImageURI(null);
		imgSelected.dispose();

		initScreen();
	}

	@Override
	public void onClick(View view) {
		switch (iState) {
			case STATE_UPPER:
				if(btYes.equals(view)) {
					isUpper = true;
				} else if(btNo.equals(view)) {
					isUpper = false;
				} else
					Logger.error("Unknown assessment error");

				//----- 상지 평가 완료 -----//
				txtState.setText(R.string.app_text_guide_assessment_lower);
				iState = STATE_LOWER;
				break;
			case STATE_LOWER:
				if(btYes.equals(view)) {
					isLower = true;
				} else if(btNo.equals(view)) {
					isLower = false;
				} else
					Logger.error("Unknown assessment error");

				//---- 평가 완료 -----//
				Intent result = new Intent();
				Logger.debug("Result -> " + isUpper + ", " + isLower);
				result.putExtra("Assessment Result", isUpper && isLower);
				setResult(RESULT_OK, result);
				finish();
				break;
			default:
				Logger.error("Unknown assessment state");
				break;
		}
	}
}
