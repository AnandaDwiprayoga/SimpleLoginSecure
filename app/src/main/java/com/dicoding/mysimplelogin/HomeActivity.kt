package com.dicoding.mysimplelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mysimplelogin.databinding.ActivityHomeBinding
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import id.pasukanlangit.core.SessionManager
import id.pasukanlangit.core.UserRepository
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sesi = SessionManager(this)
        userRepository = UserRepository.getInstance(sesi)

        binding.tvWelcome.text = "Welcome ${userRepository.getUser()}"

        binding.btnLogout.setOnClickListener {
            userRepository.logoutUser()
            moveToMainActivity()
        }

        binding.fab.setOnClickListener {
            try {
//                moveToChatActivity()
                installChatModule()
            }catch (e: Exception){
                Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun installChatModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleChat = "chat"
        if(splitInstallManager.installedModules.contains(moduleChat)){
            moveToChatActivity()
            Toast.makeText(this,"Open Module", Toast.LENGTH_SHORT).show()
        }else{
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleChat)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(this,"Success installing module", Toast.LENGTH_SHORT).show()
                    moveToChatActivity()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error installing module",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun moveToChatActivity() {
        startActivity(Intent(this, Class.forName("id.pasukanlangit.mysimplelogin.chat.ChatActivity")))
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
