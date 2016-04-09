package jiudeng.me.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	/**
	 * ÏÔÊ¾×ÖÄ¸µÄTextView
	 */
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/**
	 * ºº×Ö×ª»»³ÉÆ´ÒôµÄÀà
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ¸ù¾ÝÆ´ÒôÀ´ÅÅÁÐListViewÀïÃæµÄÊý¾ÝÀà
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		// //ÊµÀý»¯ºº×Ö×ªÆ´ÒôÀà
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//ÉèÖÃÓÒ²à´¥Ãþ¼àÌý
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//¸Ã×ÖÄ¸Ê×´Î³öÏÖµÄÎ»ÖÃ
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//ï¿½ï¿½ï¿½ï¿½Òªï¿½ï¿½ï¿½ï¿½adapter.getItem(position)ï¿½ï¿½ï¿½ï¿½È¡ï¿½ï¿½Ç°positionï¿½ï¿½ï¿½ï¿½Ó¦ï¿½Ä¶ï¿½ï¿½ï¿½
				Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});
		
		SourceDateList = filledData(getResources().getStringArray(R.array.date));	//Ìî³äÊý¾Ý
		
		// ¸ù¾Ýa-z½øÐÐÅÅÐòÔ´Êý¾Ý
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Öµï¿½Ä¸Ä±ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿?
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ÖµÎªï¿½Õ£ï¿½ï¿½ï¿½ï¿½ï¿½ÎªÔ­ï¿½ï¿½ï¿½ï¿½ï¿½Ð±ï¿½ï¿½ï¿½ï¿½ï¿½Îªï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ð±ï¿½
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * ÎªListViewÌî³äÊý¾Ý
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//ºº×Ö×ª»»³ÉÆ´Òô
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ÕýÔò±í´ïÊ½£¬ÅÐ¶ÏÊ××ÖÄ¸ÊÇ·ñÊÇÓ¢ÎÄ×ÖÄ¸
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * ¸ù¾ÝÊäÈë¿òÖÐµÄÖµÀ´¹ýÂËÊý¾Ý²¢¸üÐÂListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ¸ù¾Ýa-z½øÐÐÅÅÐò
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
