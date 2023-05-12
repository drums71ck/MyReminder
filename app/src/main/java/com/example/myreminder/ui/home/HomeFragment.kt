package com.example.myreminder.ui.home

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.RelativeLayout

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myreminder.R
import com.example.myreminder.databinding.FragmentHomeBinding
import java.util.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var pickerColor : PopupWindow? = null
    lateinit var btnColorPicker : ImageButton
    lateinit var btnTextColor: ImageButton
    lateinit var btnClock: ImageButton
    lateinit var txtTittle: EditText
    lateinit var txtContent: EditText
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val postItLayout = root.findViewById<RelativeLayout>(R.id.postItLayout)
        btnColorPicker = root.findViewById(R.id.colorButton)
        txtTittle = root.findViewById(R.id.titleEditText)
        txtContent = root.findViewById(R.id.contentEditText)
        btnTextColor = root.findViewById(R.id.textColorButton)
        btnColorPicker.setOnClickListener(){
            randomColor(postItLayout)
        }
        btnTextColor.setOnClickListener(){
            randomTextColor()
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun randomColor(postItLayout: RelativeLayout) {
        val rnd = Random()
        val colorT: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        postItLayout.setBackgroundColor(colorT)
    }
    fun randomTextColor() {
        val rnd = Random()
        val colorT: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        txtContent.setTextColor(colorT)
        txtTittle.setTextColor(colorT)
    }



}