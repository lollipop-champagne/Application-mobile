package org.lollipopchampagne;

import java.io.File;

import org.lollipopchampagne.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class HomeActivity extends Activity {

	private static int SPLASH_TIME_OUT = 3000;
	 
	ImageView iv1;
	AnimationDrawable Anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);        
    
 
        iv1 = (ImageView) findViewById(R.id.logoHS);
        try {
        	
         //Bitmap bitmapResized = Bitmap.createScaledBitmap((R.drawable.full_logo2), 500, 500, false);	
         
         
         BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
           R.drawable.full_logo);
         BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
           R.drawable.full_logo2);      
         
         
         Anim = new AnimationDrawable();
         Anim.addFrame(frame1, 750);
         Anim.addFrame(frame2, 750);
         Anim.setOneShot(false);
         iv1.setBackgroundDrawable(Anim);
         final Handler handler = new Handler();
         handler.postDelayed(new Runnable() {

          public void run() {
            
           Anim.start();
            
          }
         }, 200);
        } catch (Exception e) {
        	   // TODO: handle exception
        	  }
        
        new Handler().postDelayed(new Runnable() {
        	 
            
            /** Showing splash screen with a timer. This will be useful when you
            * want to show case your app logo / company*/
            

           @Override
           public void run() {
               // This method will be executed once the timer is over
               // Start your app main activity
        	   
        	   File humaStock = new File("/sdcard/cacheHumaStock/");
        	   if(!humaStock.exists()){
        		   
        		   humaStock.mkdir();
        		   
        	   }
        	   
        	   Intent i;
        	   if(FileUtils.readFile("/sdcard/cacheHumaStock/connecte.txt").length()== 0){
        		   
        		   i = new Intent(HomeActivity.this, ConnexionA.class);
        		   
        	   }else{
        		   
        		   
        		   i = new Intent(HomeActivity.this, AccueilActivity.class);
        		   
        	   }
        	   
               startActivity(i);
               Anim.stop();
               // close this activity
               finish();
           }
       }, SPLASH_TIME_OUT);
        
       }
}

