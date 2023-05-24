package com.example.myreminder.ui.dashboard

import DataBaseConnection
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myreminder.MainActivity
import com.example.myreminder.Minigame
import com.example.myreminder.R
import com.example.myreminder.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnHidePassword: ImageButton
    private lateinit var btnShowPassword: ImageButton
    private lateinit var btnGame: Button
    private lateinit var passwordEditText: EditText
    private lateinit var dbHelper: DataBaseConnection

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        dbHelper = DataBaseConnection(requireContext())

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar Texto
        txtName = root.findViewById(R.id.txtName)
        txtEmail = root.findViewById(R.id.txtEmail)
        passwordEditText = root.findViewById(R.id.passwordEditText)

        // Inicializar Botones

        btnHidePassword = root.findViewById(R.id.btnHidePassword)
        btnShowPassword = root.findViewById(R.id.btnShowPassword)
        btnUpdatePassword = root.findViewById(R.id.btnUpdatePass)
        btnGame = root.findViewById(R.id.btnGame)

        init()

        return root
    }

    private fun init() {
        chargeDades()
        chargeButton()

    }

    private fun chargeButton() {
        btnHidePassword.isVisible = false

        btnShowPassword.setOnClickListener {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            btnHidePassword.isVisible = true
            btnShowPassword.isVisible = false
        }

        btnHidePassword.setOnClickListener {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            btnHidePassword.isVisible = false
            btnShowPassword.isVisible = true
        }
        btnGame.setOnClickListener(){
            val intent = Intent(requireContext(), Minigame::class.java)
            startActivity(intent)
        }

    }

    private fun chargeDades() {
        var email = MainActivity.bestEmail
        var username = dbHelper.getUsernameByEmail(email)
        var password = dbHelper.getPasswordByEmail(email)

        txtName.text = username
        txtEmail.text = email
        passwordEditText.setText(password)

        btnUpdatePassword.setOnClickListener(){
            val passNew = passwordEditText.text.toString()
            dbHelper.updatePassword(email,passNew)

        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}