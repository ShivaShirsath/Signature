package ss.shiva.signature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View{
	private Path drawPath;
	private Paint drawPaint,canvasPaint;
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	public DrawingView(Context context,AttributeSet attrs){
        super(context,attrs);
        setupDrawing(); 
   }
	public void startNew(){
        drawCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }
	public void setErase(Boolean isErase){
		if(isErase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		else drawPaint.setXfermode(null);
	}
	@Override
	protected void onSizeChanged ( int w, int h, int oldw, int oldh ){
		super.onSizeChanged ( w, h, oldw, oldh );		
		canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
		canvas.drawPath(drawPath,drawPaint);
	}
	@Override
	public boolean onTouchEvent ( MotionEvent event ){	
		float touchX = event.getX(), touchY = event.getY();	
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN : drawPath.moveTo(touchX,touchY); break;
			case MotionEvent.ACTION_MOVE : drawPath.lineTo(touchX,touchY); break;
			case MotionEvent.ACTION_UP :
                drawCanvas.drawPath(drawPath,drawPaint);
                drawPath.reset(); 
            break;
			default : return false;
		}
		invalidate();
		return true;
	}
	public void setStrokeColor(int newColor){
		drawPaint.setColor(newColor);
	} 
    public void setStyle(int position){
        switch(position){
            case 1: drawPaint.setStyle(Paint.Style.FILL); break;
            case 2: drawPaint.setStyle(Paint.Style.STROKE); break;
            case 3: drawPaint.setStyle(Paint.Style.FILL_AND_STROKE); break;      
        }
    }
    public void setStrokeWidth(float width){
        drawPaint.setStrokeWidth(width);
    }
    public void setStrokeJoin(int position){
        switch(position){
            case 1: drawPaint.setStrokeJoin(Paint.Join.BEVEL); break;
            case 2: drawPaint.setStrokeJoin(Paint.Join.MITER); break;
            case 3: drawPaint.setStrokeJoin(Paint.Join.ROUND); break;      
        }
    }
    public void setStrokeCap(int position){
        switch(position){
            case 1: drawPaint.setStrokeCap(Paint.Cap.BUTT); break;
            case 2: drawPaint.setStrokeCap(Paint.Cap.ROUND); break;
            case 3: drawPaint.setStrokeCap(Paint.Cap.SQUARE); break;      
        }
    }
	public void setupDrawing(){
		drawPath = new Path();
		drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
		setStrokeColor(Color.parseColor("#000000"));
		setStrokeWidth(5);
		setStyle(2);
		setStrokeJoin(3);
		setStrokeCap(2);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
}
