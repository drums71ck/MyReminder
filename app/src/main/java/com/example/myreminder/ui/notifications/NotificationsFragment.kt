package com.example.myreminder.ui.notifications

import DataBaseConnection
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        btnDropAll.setOnClickListener(){
            dbHelper.deleteAllPostIt()
        }
        btnDropAccount.setOnClickListener(){
            createAlert()
        }


        return root
    }

    private fun createAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.strQuestionDrop))
            .setMessage(resources.getString(R.string.strQuestionDescription))
            .setNegativeButton("No"){ dialog, which ->

            }
            .setPositiveButton("Ok"){ dialog, which ->
                val email = MainActivity.bestEmail
                dbHelper.deleteUserByEmail(email)
                goTologin()

            }.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun goTologin(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}