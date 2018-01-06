package com.project.eti.emusial.a3dmodelsviewer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.project.eti.emusial.a3dmodelsviewer.helpers.SharedParameters;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

	protected OGLSurfaceView glSurfaceView;
    protected SeekBar seekBar;
    protected ViewGroup parent;
    protected int viewIndex;

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

        View tempView = findViewById(R.id.tempView);
        parent = (ViewGroup) tempView.getParent();
        viewIndex = parent.indexOfChild(tempView);
        parent.removeView(tempView);
        glSurfaceView = new OGLSurfaceView(this);
        parent.addView(glSurfaceView, viewIndex);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int light_strength = SharedParameters.getLightStrength();
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light_strength = (seekBar.getProgress() + 20) / 20;
                Log.d("STRENGTH", Integer.toString(light_strength));

                float[] color = new float[]{ light_strength, light_strength, light_strength, 1 };
                SharedParameters.setAction(SharedParameters.getAction());
                SharedParameters.setLightColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_load_file) {

            parent.removeView(glSurfaceView);
            glSurfaceView = new OGLSurfaceView(MainActivity.this);
            parent.addView(glSurfaceView, viewIndex);

            return true;

        } else if (id == R.id.action_exit) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String action = null;
        SharedParameters sharedParameters = new SharedParameters();
        seekBar.setVisibility(View.INVISIBLE);

        if (id == R.id.nav_rotate_object) {
            action = "rotate_object";
        } else if (id == R.id.nav_rotate_light) {
            action = "rotate_light";

        } else if (id == R.id.nav_light_strength) {
            action = "light strength";
            seekBar.setVisibility(View.VISIBLE);
            if (seekBar.getProgress() == 0) {
                seekBar.setProgress(20);
            }

        } else if (id == R.id.nav_color_white) {
            float[] white = new float[]{ 1, 1, 1, 1 };
            action = SharedParameters.getAction();
            SharedParameters.setLightColor(white);

        } else if (id == R.id.nav_color_red) {
            float[] red = new float[]{ 1, 0, 0, 1 };
            action = SharedParameters.getAction();
            SharedParameters.setLightColor(red);

        } else if (id == R.id.nav_tex_wood) {
            action = SharedParameters.getAction();
            SharedParameters.setTexture(R.drawable.wood);

        } else if (id == R.id.nav_tex_bamboo) {
            action = SharedParameters.getAction();
            SharedParameters.setTexture(R.drawable.bamboo_curtain);

        } else if (id == R.id.nav_tex_lava) {
            action = SharedParameters.getAction();
            SharedParameters.setTexture(R.drawable.lava);

        }

        Log.d("ACTION", action);
        SharedParameters.setAction(action);
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
