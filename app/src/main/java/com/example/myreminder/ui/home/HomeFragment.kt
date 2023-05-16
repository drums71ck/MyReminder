package com.example.myreminder.ui.home

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
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
    lateinit var btnAddPostIt : ImageButton
    lateinit var txtTittle: EditText
    lateinit var txtContent: EditText
    lateinit var containerPostIt : LinearLayout
    private var postItCount = 0
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

        // Definimos variables
        btnColorPicker = root.findViewById(R.id.colorButton)
        txtTittle = root.findViewById(R.id.titleEditText)
        txtContent = root.findViewById(R.id.contentEditText)
        btnTextColor = root.findViewById(R.id.textColorButton)
        btnAddPostIt = root.findViewById(R.id.add_fragment_button)
        containerPostIt = root.findViewById(R.id.containerPostIt)


        // Controladores de click de los botones
        btnColorPicker.setOnClickListener(){
            randomColor(postItLayout)
        }
        btnTextColor.setOnClickListener(){
            randomTextColor()
        }
        btnAddPostIt.setOnClickListener(){
            addPostItLayout()
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

    /**
     * Esta Función lo que hace en pocas palabras es clonar los atributos del post_it
     * agregandolo nueva id
     * @sample so
     */
    private fun addPostItLayout() {
        postItCount++
        val inflater = LayoutInflater.from(requireContext())
        val postItView = inflater.inflate(R.layout.post_it, containerPostIt, false)
        val uniqueID = View.generateViewId()
        postItView.id = uniqueID

        // Obtenemos el layout y tamaño del post IT
        val postItLayout = requireView().findViewById<RelativeLayout>(R.id.postItLayout)
        val postItWidth = postItLayout.width
        val postItHeight = postItLayout.height


        // Le decimos que al crear mantenga un espacio y se situe en el centro
        val layoutParams = LinearLayout.LayoutParams(postItWidth, postItHeight)
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.post_it_margin_top)
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL

        postItView.layoutParams = layoutParams

        // Cargamos los botones creados de cada clone
        val colorButton = postItView.findViewById<ImageButton>(R.id.colorButton)
        val textColorButton = postItView.findViewById<ImageButton>(R.id.textColorButton)
        val addButton = postItView.findViewById<ImageButton>(R.id.add_fragment_button)
        val txtTittleClone = postItView.findViewById<EditText>(R.id.titleEditText)
        val txtContentClone = postItView.findViewById<EditText>(R.id.contentEditText)

        colorButton.setOnClickListener {
            randomColor(postItView as RelativeLayout)
        }
        textColorButton.setOnClickListener {

            val rnd = Random()
            val colorT: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

            txtTittleClone.setTextColor(colorT)
            txtContentClone.setTextColor(colorT)

        }
        addButton.setOnClickListener {
            addPostItLayout()
        }

        containerPostIt.addView(postItView)
    }


}