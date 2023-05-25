package com.example.myreminder.ui.dashboard

import DataBaseConnection
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Nickname
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

/**
 * @author drums71ck
 * Este fragmento se encargara del perfil
 * tendra que mostrar el email y nombre del usuario
 * ademas que tendra un apartado de minijuego por si quiere
 * aliviar sus problemas pegandole a botonsitos
 */
class DashboardFragment : Fragment() {
    // Definimos las pariables
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnHidePassword: ImageButton
    private lateinit var btnShowPassword: ImageButton
    private lateinit var btnGame: Button
    private lateinit var passwordEditText: EditText
    private lateinit var dbHelper: DataBaseConnection
    private lateinit var username: String
    private lateinit var password: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * aca es donde inicializaremos todos las
     * variables del layout del perfil
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Instanciamos la base de datos
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

    /**
     * Sera nuestro main donde cargaremos todas las funciones
     */
    private fun init() {
        chargeDades()
        chargeButton()

    }


    /**
     * Inicializamos todos los botones y le damos una accion
     */
    private fun chargeButton() {
        btnHidePassword.isVisible = false

        /*
        Cada vez que se presione el icono mostrara o escondera la contraseña
        dependiendo el estado de la misma
         */
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
        // Mandara al activity de Minigame
        btnGame.setOnClickListener(){
            val intent = Intent(requireContext(), Minigame::class.java)
            startActivity(intent)
        }

    }

    /**
     * Esta funcion cargara los string y añadira sus
     * respectivos valores
     */
    private fun chargeDades() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val savedSession = sharedPreferences.getString("email","")
        var email = savedSession
        var emailb = MainActivity.bestEmail
        if (email != ""){
            username = dbHelper.getUsernameByEmail(email!!)!!
            password = dbHelper.getPasswordByEmail(email!!)!!
            txtEmail.text = email

        }else{
            username = dbHelper.getUsernameByEmail(emailb!!)!!
            password = dbHelper.getPasswordByEmail(emailb!!)!!
            txtEmail.text = emailb

        }
        txtName.text = username

        passwordEditText.setText(password)

        btnUpdatePassword.setOnClickListener(){
            val passNew = passwordEditText.text.toString()
            if(email != ""){
                dbHelper.updatePassword(email!!,passNew)
            }else{
                dbHelper.updatePassword(emailb!!,passNew)
            }


        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}