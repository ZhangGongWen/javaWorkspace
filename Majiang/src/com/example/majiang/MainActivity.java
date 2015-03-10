package com.example.majiang;

import java.util.ArrayList;
import java.util.HashMap;

import MyDraw.MyView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button mConnect;
	private RelativeLayout layout;
	public  HashMap <Integer,Integer> ImageViewMap;
	public  ArrayList<MyView> mImageViewArray;
	public  ArrayList<Integer> mChoice;////点击后选择吃或者碰的牌
	DisplayMetrics dm = new DisplayMetrics();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    layout = (RelativeLayout)findViewById(R.id.MainLayout);			
		mConnect = (Button) findViewById(R.id.button1);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mConnect.setOnClickListener(new MyButtonListener());
		DrawBackGround(R.drawable.big,layout);
		mConnect.bringToFront();
		//mConnect.setClickable(clickable);
		//mConnect.setVisibility(View.VISIBLE);
	}
	public void InitViewArray()
	{
		mImageViewArray = new ArrayList<MyView>();
		int leftPosition =(int) (10*dm.density);
		int topPosition  =(int) (200*dm.density);
		for(int i = 0;i<=8;i++)
		{
			MyView temp = new MyView(MainActivity.this);
			leftPosition+=temp.Init(ImageViewMap.get(i), R.drawable.mj_bg, new Rect(leftPosition,topPosition,200,200));
			mImageViewArray.add(temp);
		}
		for(int i = 10;i<=14;i++)
		{
			MyView temp = new MyView(MainActivity.this);
			leftPosition+=temp.Init(ImageViewMap.get(i), R.drawable.mj_bg, new Rect(leftPosition,topPosition,200,200));
			mImageViewArray.add(temp);
		}		
	}
	public void InitMap()
	{
		ImageViewMap = new HashMap<Integer,Integer>();
		ImageViewMap.put(0, R.drawable.tong_1);
		ImageViewMap.put(1, R.drawable.tong_2);
		ImageViewMap.put(2, R.drawable.tong_3);
		ImageViewMap.put(3, R.drawable.tong_4);
		ImageViewMap.put(4, R.drawable.tong_5);
		ImageViewMap.put(5, R.drawable.tong_6);
		ImageViewMap.put(6, R.drawable.tong_7);
		ImageViewMap.put(7, R.drawable.tong_8);
		ImageViewMap.put(8, R.drawable.tong_9);
		ImageViewMap.put(10, R.drawable.tiao_1);
		ImageViewMap.put(11, R.drawable.tiao_2);
		ImageViewMap.put(12, R.drawable.tiao_3);
		ImageViewMap.put(13, R.drawable.tiao_4);
		ImageViewMap.put(14, R.drawable.tiao_5);
		ImageViewMap.put(15, R.drawable.tiao_6);
		ImageViewMap.put(16, R.drawable.tiao_7);
		ImageViewMap.put(17, R.drawable.tiao_8);
		ImageViewMap.put(18, R.drawable.tiao_9);
		ImageViewMap.put(20, R.drawable.w_1);
		ImageViewMap.put(21, R.drawable.w_2);
		ImageViewMap.put(22, R.drawable.w_3);
		ImageViewMap.put(23, R.drawable.w_4);
		ImageViewMap.put(24, R.drawable.w_5);
		ImageViewMap.put(25, R.drawable.w_6);
		ImageViewMap.put(26, R.drawable.w_7);
		ImageViewMap.put(27, R.drawable.w_8);
		ImageViewMap.put(28, R.drawable.w_9);
		ImageViewMap.put(30, R.drawable.dong);
		ImageViewMap.put(40, R.drawable.nan);
		ImageViewMap.put(50, R.drawable.xi);
		ImageViewMap.put(60, R.drawable.bei);
		ImageViewMap.put(70, R.drawable.zhong);
		ImageViewMap.put(80, R.drawable.fa);
		ImageViewMap.put(90, R.drawable.bai);
	}
	public void DrawBackGround(int imgId,RelativeLayout myLayout)
	{		
		//dm = getResources().getDisplayMetrics();
		float screenWidth = dm.widthPixels;
		float screenHeight = dm.heightPixels;
		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),imgId);
		float bitmapWidth = bitmapOrg.getWidth();
		float bitmapHeight = bitmapOrg.getHeight();
		float scaleWidth =(screenWidth/bitmapWidth);
		float scaleHeight=(screenHeight/bitmapHeight);
		Matrix matrix = new Matrix();	
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg,0,0,(int)bitmapWidth,(int)bitmapHeight,matrix,true);
		ImageView imgView  = new ImageView(this) ;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); 
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//与父容器的左侧对齐  
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);//与父容器的上侧对齐
		imgView.setImageBitmap(resizedBitmap);
		imgView.setLayoutParams(lp);
		myLayout.addView(imgView);
	}
	class MyButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mConnect.setVisibility(View.INVISIBLE);
			//DrawBackGround(R.drawable.big,layout);
			//DrawBackGround(R.drawable.share_09,layout);
			//layout.addView(test);			
			for(int i = 0;i<14;i++)
			{
				layout.addView(mImageViewArray.get(i));
			}
			Log.e("TAG" + "  DisplayMetrics(111)", "dm.density=" + dm.density +" 2left="+mImageViewArray.get(1).getLeft()
					+"2top"+mImageViewArray.get(1).getTop()+"width"+mImageViewArray.get(1).getWidth()+ "\n");
			
			//test.invalidate();
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		InitMap();
		InitViewArray();
		//test =new MyView(MainActivity.this);
		//test.Init(R.drawable.dong, R.drawable.mj_bg,new Rect((int)(50*dm.density),(int)(200*dm.density),300,300) );/*new Rect((int)(50*dm.density),(int)(200*(dm.density)),200,200)*/
		
		super.onStart();
	}
}
