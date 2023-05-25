package com.example.myreminder.ui.notifications

import DataBaseConnection
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myreminder.MainActivity
import com.example.myreminder.R
import com.example.myreminder.databinding.FragmentNotificationsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author drumstick
 * Este fragmento esta encargada de los ajustes
 * de la aplicación
 *
 */
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var btnDropAll: Button
    private lateinit var dbHelper : DataBaseConnection
    private lateinit var btnDropAccount: Button
    private lateinit var btnCloseSession: Button
    private lateinit var switchVibration: Switch

    companion object{
        var flagVibration = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        dbHelper = DataBaseConnection(requireContext())
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnDropAll = root.findViewById(R.id.btnDropAllNotes)
        btnDropAccount = root.findViewById(R.id.btnDropAccount)
        btnCloseSession = root.findViewById(R.id.btnCloseSession)
        switchVibration = root.findViewById(R.id.switchVibrate)

        btnDropAll.setOnClickListener(){
            dbHelper.deleteAllPostIt()
        }
        btnDropAccount.setOnClickListener(){
            createAlert()
        }
        btnCloseSession.setOnClickListener(){
            closeSession()
            goTologin()
        }
        switchVibration.isChecked = flagVibration
        switchVibration.setOnCheckedChangeListener() { _, isChecked ->

            if (isChecked){
                flagVibration = true
            }else{
                flagVibration = false
            }
        }


        return root
    }

    /**
     * Esta función lo que hara es cuando el usuario
     * le de al boton de borrar cuenta  le aparezca
     * una ventana de confirmación
     *
     */
    private fun createAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.strQuestionDrop))
            .setMessage(resources.getString(R.string.strQuestionDescription))
            .setNegativeButton("No"){ dialog, which ->

            }
            .setPositiveButton("Ok"){ dialog, which ->
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val savedSession = sharedPreferences.getString("email","")
                var email = savedSession
                val emailb = MainActivity.bestEmail
                if (email != ""){
                    dbHelper.deleteUserByEmail(email!!)
                    closeSession()
                    goTologin()
                }else{
                    dbHelper.deleteUserByEmail(emailb!!)
                    closeSession()
                    goTologin()
                }


            }.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun closeSession() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        editor.remove("saved_session")
        editor.remove("email")
        editor.apply()

        // Aquí puedes realizar otras acciones relacionadas con el cierre de sesión, como redirigir a la pantalla de inicio de sesión.
    }

    fun goTologin(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}