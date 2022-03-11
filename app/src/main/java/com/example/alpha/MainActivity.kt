package com.example.alpha

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    companion object{
        lateinit var context : Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_transaction, R.id.nav_lines, R.id.nav_users), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_export_data -> {
                val backupDBPath: String = "$dataDir/databases"
                val f1 = File("$obbDir/databases/alpha");
                val f2 = File("$obbDir/databases/alpha-shm");
                val f3 = File("$obbDir/databases/alpha-wal");
                if(f1.exists()){f1.delete()}
                if(f2.exists()){f2.delete()}
                if(f3.exists()){f3.delete()}
                File("$backupDBPath/alpha").copyTo(f1)
                File("$backupDBPath/alpha-shm").copyTo(f2)
                File("$backupDBPath/alpha-wal").copyTo(f3)
                val snackBar = Snackbar.make(findViewById(R.id.nav_host_fragment), "Data Exported to '../Android/obb/com.example.alpha/databases'.", Snackbar.LENGTH_LONG)
                snackBar.setAction("", null
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}