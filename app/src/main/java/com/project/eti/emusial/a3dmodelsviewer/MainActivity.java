package com.project.eti.emusial.a3dmodelsviewer;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.project.eti.emusial.a3dmodelsviewer.helpers.SharedParameters;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

	protected OGLSurfaceView glSurfaceView;
    private SharedParameters sharedParameters = new SharedParameters();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        glSurfaceView = new OGLSurfaceView(this);
        //setContentView(glSurfaceView);

        View tempView = findViewById(R.id.tempView);
        ViewGroup parent = (ViewGroup) tempView.getParent();
        int index = parent.indexOfChild(tempView);
        parent.removeView(tempView);
        parent.addView(glSurfaceView, index);

        /*
        LinearLayout linearLayout = new LinearLayout(this);

        Button rotateObject = new Button(this);
        rotateObject.setText("Rotate object");
        linearLayout.addView(rotateObject);
        linearLayout.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        Button rotateLight = new Button(this);
        rotateLight.setText("Rotate light");
        linearLayout.addView(rotateLight);
        linearLayout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);

        this.addContentView(linearLayout,
                new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT));

        rotateObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedParameters.getObjectButton()) {
                    sharedParameters.setObjectButton(true);
                    sharedParameters.setLightButton(false);
                }
            }
        });

        rotateLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedParameters.getLightButton()) {
                    sharedParameters.setLightButton(true);
                    sharedParameters.setObjectButton(false);
                }
            }
        });
        */
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_load_file) {
            return true;
        } else if (id == R.id.action_exit) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String action = null;

        if (id == R.id.nav_rotate_object) {
            action = "rotate_object";
        } else if (id == R.id.nav_rotate_light) {
            action = "rotate_light";

        } else if (id == R.id.nav_light_strength) {
            action = "light strength";

        } else if (id == R.id.nav_color_white) {
            action = "color white";
            float[] white = new float[]{ 1, 1, 1, 1 };
            SharedParameters.setLightColor(white);

        } else if (id == R.id.nav_color_red) {
            action = "color red";
            float[] red = new float[]{ 1, 0, 0, 1 };
            SharedParameters.setLightColor(red);

        }

        Log.d("ACTION", action);
        SharedParameters sharedParameters = new SharedParameters();
        sharedParameters.setAction(action);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

	@Override
	protected void onResume() 
	{
		super.onResume();
		glSurfaceView.onResume();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		glSurfaceView.onPause();
	}
}
