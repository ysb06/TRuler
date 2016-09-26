package pe.hgs.truler.phase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.hgs.truler.R;

public class TaskInfo extends AppCompatActivity implements View.OnClickListener {

	private Button btNextCamera;
	private Button btNextSelection;

	private EditText etParam01;
	private EditText etParam02;
	private EditText etParam03;
	private EditText etParam04;
	private EditText etParam05;
	private EditText etParam06;
	private EditText etParam07;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_info);
		setTitle("정보 입력");

		btNextCamera = (Button) findViewById(R.id.btTI_next_camera);
		btNextSelection = (Button) findViewById(R.id.btTI_next_selection);
		btNextCamera.setOnClickListener(this);
		btNextSelection.setOnClickListener(this);

		etParam01 = (EditText) findViewById(R.id.etTI_param_01);
		etParam02 = (EditText) findViewById(R.id.etTI_param_02);
		etParam03 = (EditText) findViewById(R.id.etTI_param_03);
		etParam04 = (EditText) findViewById(R.id.etTI_param_04);
		etParam05 = (EditText) findViewById(R.id.etTI_param_05);
		etParam06 = (EditText) findViewById(R.id.etTI_param_06);
		etParam07 = (EditText) findViewById(R.id.etTI_param_07);
	}

	@Override
	public void onClick(View view) {		//이벤트가 버튼 하나에만 반응하므로 뷰 검사는 하지 않음
		Intent result = new Intent();
		result.putExtra("TaskInfo_01_Name", etParam01.getText().toString());
		result.putExtra("TaskInfo_02_Crop", etParam02.getText().toString());
		result.putExtra("TaskInfo_03_Task", etParam03.getText().toString());
		result.putExtra("TaskInfo_04_SubTask", etParam04.getText().toString());
		result.putExtra("TaskInfo_05_Location", etParam05.getText().toString());
		String sNum = etParam06.getText().toString();
		if(sNum.length() != 0)
			result.putExtra("TaskInfo_06_WorkTime", Integer.parseInt(sNum));
		else
			result.putExtra("TaskInfo_06_WorkTime", 0);
		result.putExtra("TaskInfo_07_Checker", etParam07.getText().toString());

		if(view.equals(btNextCamera)) {
			setResult(10, result);
		} else {
			setResult(11, result);
		}
		finish();
	}
}
