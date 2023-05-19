package com.example.myreminder.ui.notifications

import DataBaseConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myreminder.R
import com.example.myreminder.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var btnDropAll: Button
    private lateinit var dbHelper : DataBaseConnection

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
        btnDropAll = root.findViewById<Button>(R.id.btnDropAllNotes)

        btnDropAll.setOnClickListener(){
            dbHelper.deleteAllPostIt()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}