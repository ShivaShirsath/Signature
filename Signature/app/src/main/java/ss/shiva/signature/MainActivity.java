package ss.shiva.signature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import com.pes.androidmaterialcolorpickerdialog.*;
import android.widget.*;
import android.app.*;
import android.content.pm.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {
	
	DrawerLayout drawer_layout;
	NavigationView left_nav,right_nav;
	String link,pack;
	long backPressedTime=0;
	boolean New=false;
	DrawingView drawing;
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
        
		try{
			drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawing = (DrawingView)findViewById(R.id.drawing);

            left_nav=(NavigationView)findViewById(R.id.left_nav);
            left_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item){
                        switch(item.getItemId()){
                            case R.id.item_newFile  : setBrushSize("New");   break;
                            case R.id.item_edit     : setBrushSize("Brush"); break;
                            case R.id.item_erase    : setBrushSize("Erase"); break;
                            case R.id.item_saveFile : setBrushSize("Save");  break;
                            case R.id.item_shareFile: setBrushSize("Share");  break;

                            case R.id.item_shareApp : shareApk(MainActivity.this); break;
                            case R.id.item_aboutApp : startActivity(new Intent(MainActivity.this,InfoActivity.class)); break;
                        }
                        return true;
                    }
                });
            right_nav=(NavigationView)findViewById(R.id.right_nav);
            right_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item){
                        switch(item.getItemId()){
                            case R.id.item_insta            : link="http://instagram.com/_shiva_shirsath__";                   pack="com.instagram.android"; break;
                            case R.id.item_instaBioWebsite  : link="http://taphere.bio/shivashirsath";                     pack="com.android.chrome"; break;
                            case R.id.item_fb               : link="https://www.facebook.com/Shiva.Shirsath.25";               pack="com.android.chrome";   break;
                            case R.id.item_telegram_group   : link="https://t.me/joinchat/RAvuYRzJzjii5unk3skY9w";             pack="org.telegram.messenger"; break;
                            case R.id.item_telegram_channel : link="https://t.me/MobileAIDE";                                  pack="org.telegram.messenger"; break;
                            case R.id.item_yt_personal      : link="https://www.youtube.com/channel/UCR-q2w0WQm5i-cmH9YwS_jA"; pack="com.google.android.youtube"; break;
                            case R.id.item_yt_official      : link="https://www.youtube.com/channel/UCtxyUnbZN8q5Z3ogbClPg-w"; pack="com.google.android.youtube"; break;
                            case R.id.item_wa               : link="https://chat.whatsapp.com/Hs3wUBtvFN67pEaqGBbVSD";         pack="com.whatsapp"; break;
                        }
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)).setPackage(pack));
                        } catch (Exception e){
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });         
		}catch(Exception e){
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
    }
    public void onColorPick(final View view){
		try{
            final ColorPicker colorPicker=new ColorPicker(this);
            colorPicker.show();
            ( (Button) colorPicker.findViewById(R.id.okColorButton) )
                .setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View button){
                        if(New)drawing.setBackgroundColor(colorPicker.getColor()); else drawing.setStrokeColor(colorPicker.getColor());
                        colorPicker.dismiss();
                    }
                });        
		}catch(Exception e){
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	public void onClick(View view){
		switch(view.getId()){
			case R.id.menu_opener: drawer_layout.openDrawer(Gravity.START); break;
			case R.id.settings_opener: drawer_layout.openDrawer(Gravity.END  ); break;
		}
	}
    public static void shareApk(Activity paramActivity) {  
        try {  
            paramActivity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).setType("*/*").putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + paramActivity.getPackageManager().getApplicationInfo(paramActivity.getPackageName(),PackageManager.GET_META_DATA).publicSourceDir)), "Share it using"));
        } catch (Exception e) {
            Toast.makeText(paramActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
	public void setBrushSize(final String str){
		final AlertDialog dialog;
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		View view=getLayoutInflater().inflate(R.layout.brush_dailog,null);
        
        TextView tv_dailog=view.findViewById(R.id.tv_dailog);
        
		final SeekBar sb_dailog=view.findViewById(R.id.sb_dailog);     
        
		final LinearLayout ll_stroke = view.findViewById(R.id.ll_stroke);
        
        final RadioGroup rg_style = view.findViewById(R.id.rg_style);
        final RadioGroup rg_join = view.findViewById(R.id.rg_join);
        final RadioGroup rg_cap = view.findViewById(R.id.rg_cap);
        
		final LinearLayout ll_color=view.findViewById(R.id.ll_color);
        
        Button yes=view.findViewById(R.id.yes);
		Button no=view.findViewById(R.id.no);
        
		builder.setView(view);
		dialog = builder.create();
		dialog.setTitle(str);
		if(str.equals("New")) {
			tv_dailog.setText("Set Background Color"); 
			sb_dailog.setVisibility(View.GONE); 
            ll_stroke.setVisibility(View.GONE);
			New=true;
		}
		if(str.equals("Brush")){
			tv_dailog.setText("Select Size & Color of Brush");     
			New=false;
		}
		if(str.equals("Erase")){
			tv_dailog.setText("Select Size of Eraser");
			ll_color.setVisibility(View.GONE); 
			New=false;
		}
		if(str.equals("Save")){
			tv_dailog.setText("Save Drawing");
			sb_dailog.setVisibility(View.GONE);
			ll_color.setVisibility(View.GONE);
            ll_stroke.setVisibility(View.GONE);
			New=false;
		}
		if(str.equals("Share")){
			tv_dailog.setText("Share Drawing");
			sb_dailog.setVisibility(View.GONE);
			ll_color.setVisibility(View.GONE);
            ll_stroke.setVisibility(view.GONE);
			New=false;
		}
		yes.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(str.equals("New")){ 
						drawing.setupDrawing();    
						drawing.startNew();
					}
					if(str.equals("Brush")){
						drawing.setStrokeWidth(sb_dailog.getProgress());
                        drawing.setStyle( strokePosition( rg_style.getCheckedRadioButtonId() ) );
                        drawing.setStrokeJoin( joinPosition(rg_join.getCheckedRadioButtonId()) );
                        drawing.setStrokeCap( capPosition(rg_cap.getCheckedRadioButtonId()) );
						drawing.setErase(false);
					}
					if(str.equals("Erase")){
						drawing.setStrokeWidth(sb_dailog.getProgress());
                        drawing.setStyle( strokePosition( rg_style.getCheckedRadioButtonId() ) );
                        drawing.setStrokeJoin( joinPosition(rg_join.getCheckedRadioButtonId()) );
                        drawing.setStrokeCap( capPosition(rg_cap.getCheckedRadioButtonId()) );
						drawing.setErase(true);
					}
					if(str.equals("Save")){
						saveFile("Save");
					}
					if(str.equals("Share")){
						saveFile("Share");
					}
					dialog.dismiss();
					drawer_layout.closeDrawers();
				}
			});
		no.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					dialog.dismiss();
				}
			});
		dialog.setCancelable(false);
		dialog.show();	
	}
    public int strokePosition(int id){
        int position = 0;
        switch(id){
            case R.id.fill: position = 1; break;
            case R.id.stroke: position = 2; break;
            case R.id.fill_and_stroke: position = 3; break;
        }
        return position;
    }
    public int joinPosition(int id){
        int position = 0;
        switch(id){
            case R.id.bevel: position = 1; break;
            case R.id.miter: position = 2; break;
            case R.id.round: position = 3; break;
        }
        return position;
    }
    public int capPosition(int id){
        int position = 0;
        switch(id){
            case R.id.butt: position = 1; break;
            case R.id.roundCap: position = 2; break;
            case R.id.square: position = 3; break;
        }
        return position;
    } 
	public void saveFile(String option){
		drawing.setDrawingCacheEnabled(true);
		Bitmap bitmap = drawing.getDrawingCache();
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Signatute/";
		File dir = new File(path);
		dir.mkdirs();
		try {
			Calendar calendar=Calendar.getInstance();
			File file = new File(dir.toString() 
				+ File.separator						  + "Signature"
				+ calendar.get(Calendar.HOUR_OF_DAY		) + "h"
				+ calendar.get(Calendar.MINUTE			) + "m"
				+ calendar.get(Calendar.SECOND			) + "s_"
				+ calendar.get(Calendar.DAY_OF_MONTH	) + "-"
				+ calendar.get(Calendar.MONTH			) + "-"
				+ calendar.get(Calendar.YEAR			) + ".png"
			);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
			MediaScannerConnection.scanFile(MainActivity.this, new String[] { file.getPath() },new String[] { "image/jpeg" }, null);
			Toast saved = Toast.makeText(MainActivity.this, "Saved to " + file, Toast.LENGTH_SHORT); saved.setGravity(Gravity.CENTER,0,0); saved.show();
			drawing.destroyDrawingCache();
			
			if(option.equals("Save"))  startActivity(Intent.createChooser( new Intent().setAction(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(file),"image/png") , "Open file with"));
			if(option.equals("Share")) startActivity(Intent.createChooser( new Intent().setAction(Intent.ACTION_SEND).setDataAndType(Uri.fromFile(file), "image/*").putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)).putExtra(Intent.EXTRA_TEXT, "Share From : Signature App"), "Share Saved image using"));	
		}
		catch (Exception e) {
			Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onBackPressed(){
		if(drawer_layout.isDrawerOpen(left_nav) || drawer_layout.isDrawerOpen(right_nav)){
			drawer_layout.closeDrawers();
		}else if (backPressedTime + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
			finish();
			return;
		} 
		else {
			Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
		}
		backPressedTime = System.currentTimeMillis();
	}
	@Override
	protected void onDestroy(){
		drawing.destroyDrawingCache();
		super.onPause();
		super.onStop();
		super.onDestroy();
	}
}
