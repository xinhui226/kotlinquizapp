package com.xinhui.quizapp.ui.screen.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.xinhui.quizapp.R
import com.xinhui.quizapp.databinding.ActivityMainBinding
import com.xinhui.quizapp.ui.screen.mainActivity.viewModel.MainActivityViewModel
import com.xinhui.quizapp.ui.screen.signInUp.SignInUpActivity
import com.xinhui.quizapp.ui.screen.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setupGoogleClient(this)
        viewModel.getCurrUser()
        viewModel.getProfileUri()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController= navHostFragment.navController
        val drawerLayout=binding.drawerLayout
        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.navigationView.setNavigationItemSelectedListener { item ->
            if(item.itemId == R.id.itemLogout) {
                viewModel.logout()
                val intent = Intent(this@MainActivity, SignInUpActivity::class.java)
                startActivity(intent)
                this@MainActivity.finish()
            } else {
                navController.navigate(
                    TabContainerFragmentDirections.actionTabContainerToDashboardFragment())
                drawerLayout.close()
            }
            false
        }
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)
        updateNavUI()
    }

    fun updateNavUI(){
        lifecycleScope.launch {
            viewModel.user.collect {
                val hv = binding.navigationView.getHeaderView(0)
                val tv = hv.findViewById<TextView>(R.id.tvName)
                tv.text = it.name
            }
        }
        lifecycleScope.launch {
            viewModel.profilePic.collect {
                val hv = binding.navigationView.getHeaderView(0)
                val iv = hv.findViewById<ImageView>(R.id.ivProfilePic)
                Glide.with(hv)
                    .load(it)
                    .placeholder(R.drawable.ic_person)
                    .into(iv)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}