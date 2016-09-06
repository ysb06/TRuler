package pe.hgs.truler.phase.legacy.analysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.phase.legacy.analysis.final_result.Human;

public class FinalResult extends AppCompatActivity {

    private float[] fJointX;
	private float[] fJointY;

	private Human result;

	private TextView[] txtValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

		//UI 초기화
		txtValues = new TextView[5];
		txtValues[0] = (TextView) findViewById(R.id.txtValue1);
		txtValues[1] = (TextView) findViewById(R.id.txtValue2);
		txtValues[2] = (TextView) findViewById(R.id.txtValue3);
		txtValues[3] = (TextView) findViewById(R.id.txtValue4);
		txtValues[4] = (TextView) findViewById(R.id.txtValue5);


		//초기화
		Intent intent = getIntent();
		fJointX = intent.getFloatArrayExtra("Joint Point X");
		fJointY = intent.getFloatArrayExtra("Joint Point Y");

		result = new Human(fJointX, fJointY);
		double[] dAngles = result.getAngles();

		for(int i = 0; i < dAngles.length; i++) {
			txtValues[i].setText(String.valueOf(dAngles[i]));
		}
		//제대로 된다면 각 각도들이 표시될 것임

    }
}
