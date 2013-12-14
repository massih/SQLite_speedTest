package com.example.sqlitespeedtest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
	DbHandler dbHandler;
	Button start_btn;
	RadioGroup radio_group;
	Spinner spinner;
	Switch index_switch;
	EditText text_area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHandler = new DbHandler(this);
		start_btn = (Button) findViewById(R.id.button1);
		start_btn.setOnClickListener(start_btn_handler);

		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.Rows, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		radio_group = (RadioGroup) findViewById(R.id.radioGroup1);
		index_switch = (Switch) findViewById(R.id.switch1);
		text_area = (EditText) findViewById(R.id.editText1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	View.OnClickListener start_btn_handler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			RadioButton radio_button = (RadioButton) radio_group
					.findViewById(radio_group.getCheckedRadioButtonId());
			String test_id = (String) radio_button.getText();
			List<String[]> content = readXML((int) spinner.getSelectedItemId());
			boolean indexed = index_switch.isChecked();
//			Toast.makeText(v.getContext(), "selected test is " + indexed,
//					Toast.LENGTH_SHORT).show();
			Context context = v.getContext();
			long time=0;
			if (test_id.equalsIgnoreCase("insert")) {
				if(indexed){
//					for(int i=0;i<10;i++){
//						dbHandler.delete_indexed();
//						time += dbHandler.insert_indexed(context,content);
//					}
					dbHandler.delete_indexed();
					time = dbHandler.insert_indexed(context, content);
					text_area.append("Indexed insert average: " +(time/10)+" milliseconds \n");
				}else{
//					for(int i=0;i<10;i++){
//						dbHandler.delete_not_indexed();
//						time += dbHandler.insert_not_indexed(context,content);
//					}
//					text_area.append("Indexed insert average: " +(time/10)+" miliseconds");
					dbHandler.delete_not_indexed();
					time = dbHandler.insert_not_indexed(context, content);
					text_area.append("Insert " +time+" milliseconds \n");
				}
				
			} else if (test_id.equalsIgnoreCase("select")) {
				if(indexed){
					for(int i=0;i<10;i++){
						time += dbHandler.select_indexed();
					}
					text_area.append("Indexed select average: " +(time/10)+" milliseconds \n");
				}else{
					for(int i=0;i<10;i++){
						time += dbHandler.select_not_indexed();
					}
					text_area.append("Select average: " +(time/10)+" milliseconds \n");
				}
			} else if (test_id.equalsIgnoreCase("update")) {
				if(indexed){
					for(int i=0;i<10;i++){
						time += dbHandler.update_indexed();
					}
					text_area.append("Indexed update average: " +(time/10)+" milliseconds \n");
				}else{
					for(int i=0;i<10;i++){
						time += dbHandler.update_not_indexed();
					}
					text_area.append("Update average: " +(time/10)+" milliseconds \n");
				}
			} else if (test_id.equalsIgnoreCase("delete")) {
				if(indexed){
					time = dbHandler.delete_indexed();
					text_area.append("Indexed delete: " +(time)+" milliseconds \n");
				}else{
					time = dbHandler.delete_not_indexed();
					text_area.append("Delete: " +(time)+" milliseconds \n");
				}
			}

		}
	};

	List<String[]> readXML(int size) {
		String path;
		switch (size) {
		case 0:
			path = "1000Row.xml";
			break;
		case 1:
			path = "3000Row.xml";
			break;
		case 2:
			path = "10000Row.xml";
			break;
		default:
			path = "1000Row.xml";
			break;
		}
		List<String[]> records = new ArrayList<String[]>();
		XmlPullParser parser = Xml.newPullParser();
		AssetManager am = this.getAssets();
		try {
			InputStream in = am.open(path);
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "records");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				if (name.equals("record")) {
					records.add(readRecord(parser));
				} else {
					skip(parser);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}

	String[] readRecord(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, null, "record");
		int count = 0;
		String tel = "";
		String name = "";
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String txt = parser.getName();
			if (txt.equals("tel")) {
				parser.require(XmlPullParser.START_TAG, null, "tel");
				if (parser.next() == XmlPullParser.TEXT) {
					tel = parser.getText();
					parser.nextTag();
				}
				parser.require(XmlPullParser.END_TAG, null, "tel");
			} else if (txt.equals("name")) {
				parser.require(XmlPullParser.START_TAG, null, "name");
				if (parser.next() == XmlPullParser.TEXT) {
					name = parser.getText();
					parser.nextTag();
				}
				parser.require(XmlPullParser.END_TAG, null, "name");
			} else {
				skip(parser);
			}
		}
		String[] res = { tel, name };
		return res;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		Log.i("ELEMENTS", "SKIP CALLED !!!!!!");
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
