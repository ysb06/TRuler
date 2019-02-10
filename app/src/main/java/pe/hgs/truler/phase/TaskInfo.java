package pe.hgs.truler.phase;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.Logger;

public class TaskInfo extends AppCompatActivity implements View.OnClickListener, Phase {

	private Button btNextCamera;
	private Button btNextSelection;

	private EditText etParam01;
	private EditText etParam02;
	private EditText etParam03;
	private EditText etParam04;
	private EditText etParam05;
	private EditText etParam06;
	private EditText etParam07;
	private EditText etParam08;
	private EditText etParam09;
	private Spinner spParam10;

	private int iParam10 = 0;

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
		etParam08 = (EditText) findViewById(R.id.etTI_param_08);
		etParam09 = (EditText) findViewById(R.id.etTI_param_09);
		spParam10 = (Spinner) findViewById(R.id.spTI_param_10);
		ArrayAdapter<CharSequence> spParam10adapter = ArrayAdapter.createFromResource(this, R.array.layout_taskinfo_spinner_entries_list, android.R.layout.simple_spinner_item);
		spParam10adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spParam10.setAdapter(spParam10adapter);

		String str1 = getIntent().getStringExtra(INFO_WORKER);
		String str2 = getIntent().getStringExtra(INFO_CROP);
		String str3 = getIntent().getStringExtra(INFO_TASK);
		String str4 = getIntent().getStringExtra(INFO_SUB_TASK);
		String str5 = getIntent().getStringExtra(INFO_LOCATION);
		int time = getIntent().getIntExtra(INFO_WORK_TIME, -1);
		String str7 = getIntent().getStringExtra(INFO_ASSESSOR);

		if(str1 != null) {
			etParam01.setText(str1);
		}
		if(str2 != null) {
			etParam02.setText(str2);
		}
		if(str3 != null) {
			etParam03.setText(str3);
		}
		if(str4 != null) {
			etParam04.setText(str4);
		}
		if(str5 != null) {
			etParam05.setText(str5);
		}
		if(time != -1) {
			etParam06.setText(Integer.toString(time));
		}
		if(str7 != null) {
			etParam07.setText(str7);
		}

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
	}

	@Override
	public void onClick(View view) {		//이벤트가 버튼 하나에만 반응하므로 뷰 검사는 하지 않음
		Intent result = new Intent();
		result.putExtra(INFO_WORKER, etParam01.getText().toString());
		result.putExtra(INFO_CROP, etParam02.getText().toString());
		result.putExtra(INFO_TASK, etParam03.getText().toString());
		result.putExtra(INFO_SUB_TASK, etParam04.getText().toString());
		result.putExtra(INFO_LOCATION, etParam05.getText().toString());
		String sNum = etParam06.getText().toString();
		if(sNum.length() != 0)
			result.putExtra(INFO_WORK_TIME, Integer.parseInt(sNum));
		else
			result.putExtra(INFO_WORK_TIME, 0);
		result.putExtra(INFO_ASSESSOR, etParam07.getText().toString());
		result.putExtra(INFO_WEIGHT, etParam08.getText().toString());
		result.putExtra(INFO_ACT_POINT, etParam09.getText().toString());
		result.putExtra(INFO_NECK_BAND, spParam10.getSelectedItem().toString());


		if(view.equals(btNextCamera)) {
			setResult(10, result);
		} else {
			setResult(11, result);
		}
		finish();
	}
}
