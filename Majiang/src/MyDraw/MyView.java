package MyDraw;

import android.R;
import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.majiang.*;

public class MyView extends View {
	public int   ImageId;
	public Rect mRect;
	public boolean mClicked;
	public Bitmap mFrameBitmap;
	Bitmap mBitmap = null;                    //图像对象
	float FrameWidth;
	float FrameHeight;
	float bitmapWidthFrame;
	float bitmapHeightFrame;
	float scaleWidth ;
	float scaleHeight;
	Matrix matrix ;  //Matrix对象
	Bitmap resizedFrame;
	Bitmap resizedImage;
	RelativeLayout.LayoutParams lp;
	Context mcontext;
	DisplayMetrics dm = new DisplayMetrics();
	Paint temp = new Paint();
	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context; 
	}
	public int  Init(int id,int FrameImageId,Rect rect){
		this.setOnClickListener(new MyViewListener());
		mClicked = false;
		ImageId = id;
		matrix = new Matrix();
		mRect =new Rect();
		mRect.left = rect.left;
		mRect.top = rect.top;
		mRect.bottom = rect.bottom;
		mRect.right  = rect.bottom;
		mBitmap = ((BitmapDrawable) getResources().getDrawable(ImageId)).getBitmap(); // 从资源文件中装载图片
		mFrameBitmap=((BitmapDrawable) getResources().getDrawable(FrameImageId)).getBitmap();
		
		((MainActivity)mcontext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		FrameWidth = (float) (dm.widthPixels/18.0);
		FrameHeight = (float) (dm.heightPixels/7);
		bitmapWidthFrame  = mFrameBitmap.getWidth();
		bitmapHeightFrame = mFrameBitmap.getHeight();
		scaleWidth =(FrameWidth/bitmapWidthFrame);
		scaleHeight=(FrameWidth/bitmapHeightFrame);
		matrix.postScale(scaleWidth, scaleHeight);
		matrix.setRotate(180);
		resizedFrame = Bitmap.createBitmap(mFrameBitmap,0,0,(int)mFrameBitmap.getWidth(),(int)mFrameBitmap.getHeight(),matrix,true);
		matrix.reset();
		//matrix.postScale(scaleWidth, scaleHeight);
		resizedImage = Bitmap.createBitmap(mBitmap,0,0,(int)mBitmap.getWidth(),(int)mBitmap.getHeight(),matrix,true);
		lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); 	
		Log.e("TAG" + "  DisplayMetrics(111)", "bitmapWidthFrame=" + bitmapWidthFrame + " bitmapHeightFrame=" + bitmapHeightFrame+
				" scaleWidth="+scaleWidth+" scaleHeight="+scaleHeight+"density="+dm.density
				+"mRect.left" +mRect.left +"FrameWidth="+FrameWidth+"resizedFrameWidth="+resizedFrame.getWidth()+"\n");
		return (int) (resizedFrame.getWidth()-2*dm.density);//(int) FrameWidth;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);		
		this.setLayoutParams(lp);
		this.layout(mRect.left, mRect.top, mRect.left+resizedFrame.getWidth(),mRect.top+resizedFrame.getHeight());
		canvas.drawBitmap(resizedFrame,0 ,0, null);
		canvas.drawBitmap(resizedImage,bitmapWidthFrame/10 ,bitmapHeightFrame/4.5f, null);
		temp.setColor(Color.RED);
		temp.setTextSize(15);
		//canvas.drawCircle(100.0f,100.0f,400.0f,temp);
		//canvas.drawLine(0, 0,mRect.right,mRect.bottom, temp);
		//canvas.drawRect(0, 0, 40, 40, temp);//绘制一个矩形  
		//this.setImageBitmap(resizedFrame);
	}
	/*@Override
	public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
               Thread.sleep(100);
           }
            catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }            
            //postInvalidate();                //使用postInvalidate可以直接在线程中更新界面
        }
	}*/
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	class MyViewListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mClicked==true)
			{
				mClicked = false;
				mRect.top+=(int)(15*dm.density);
			}
			else
			{
				mClicked =true;
				mRect.top-=(int)(15*dm.density);
			}
			invalidate();
		}
	}
}
