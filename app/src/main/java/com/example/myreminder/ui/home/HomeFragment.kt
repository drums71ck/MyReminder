package com.example.myreminder.ui.home

import DataBaseConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.isInvisible

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myreminder.R
import com.example.myreminder.databinding.FragmentHomeBinding
import java.util.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var btnColorPicker : ImageButton
    lateinit var btnTextColor: ImageButton
    lateinit var btnClock: ImageButton
    lateinit var btnAddPostIt : ImageButton
    lateinit var txtTittle: EditText
    lateinit var txtContent: EditText
    lateinit var containerPostIt : LinearLayout
    lateinit var dbHelper: DataBaseConnection
    lateinit var btnDelete : ImageButton
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
        btnDelete = root.findViewById(R.id.btnTrash)


        // Controladores de click de los botones
        btnColorPicker.setOnClickListener(){
            randomColor(postItLayout)
        }
        btnTextColor.setOnClickListener(){
            randomTextColor()
        }
        btnAddPostIt.setOnClickListener(){
            dbHelper = DataBaseConnection(requireContext())
            val title = txtTittle.text.toString().trim()
            val content = txtContent.text.toString().trim()
            val color = (postItLayout.background as ColorDrawable).color
            val hexColor = String.format("#%06X", 0xFFFFFF and color)
            val id = dbHelper.insertPostIt(postItCount,title, content, hexColor)
            dbHelper.close()
   
            addPostItLayout()
        }
        btnDelete.setOnClickListener(){
            dbHelper = DataBaseConnection(requireContext())

            dbHelper.deletePostItByTitle(txtTittle.text.toString().trim())

            containerPostIt.removeView(postItLayout)
        }
        loadPostItsFromDatabase()
        return root
    }

    private fun loadPostItsFromDatabase() {
        val db = DataBaseConnection(requireContext().applicationContext)
        val postIts = db.getAllPostIts()
        db.close()

        val inflater = LayoutInflater.from(requireContext())

        for (postIt in postIts) {
            val postItView = inflater.inflate(R.layout.post_it, containerPostIt, false)
            val uniqueID = View.generateViewId()
            postItView.id = uniqueID

            // Configurar los campos de texto con los datos del post-it
            val txtTittleClone = postItView.findViewById<EditText>(R.id.titleEditText)
            val txtContentClone = postItView.findViewById<EditText>(R.id.contentEditText)
            val btnDeleteClone = postItView.findViewById<ImageButton>(R.id.btnTrash)
            val btnaddNotes = postItView.findViewById<ImageButton>(R.id.add_fragment_button)
            val color = Color.parseColor(postIt.color)

            txtTittleClone.setText(postIt.title)
            txtContentClone.setText(postIt.content)
            txtTittleClone.setTextColor(Color.BLACK)
            postItView.setBackgroundColor(color)

            // Ajustar el tamaño y el espacio del post-it
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.post_it_width),
                resources.getDimensionPixelSize(R.dimen.post_it_height)
            )
            layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.post_it_margin_top)
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            postItView.layoutParams = layoutParams

            btnaddNotes.isEnabled = false
            btnaddNotes.visibility = View.INVISIBLE

            btnDeleteClone.setOnClickListener(){
                dbHelper = DataBaseConnection(requireContext())
                dbHelper.deletePostItByTitle(txtTittleClone.text.toString().trim())
                containerPostIt.removeView(postItView)
                Log.d("yo", "pasa pero no funciona")
            }
            // Agregar el post-it al contenedor
            containerPostIt.addView(postItView)
        }

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
        Log.d("Pepe", uniqueID.toString())

        // Obtenemos el layout y tamaño del post IT
        val postItLayout = requireView().findViewById<RelativeLayout>(R.id.postItLayout)
        val postItWidth = postItLayout.width
        val postItHeight = postItLayout.height


        // Le decimos que al crear mantenga un espacio y se situe en el centro
        val layoutParams = LinearLayout.LayoutParams(postItWidth, postItHeight)
        layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.post_it_margin_top)
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL

        postItView.layoutParams = layoutParams
        val rnd = Random()
        val colorT: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        dbHelper = DataBaseConnection(requireContext())


        // Cargamos los botones creados de cada clone
        val colorButton = postItView.findViewById<ImageButton>(R.id.colorButton)
        val textColorButton = postItView.findViewById<ImageButton>(R.id.textColorButton)
        val addButton = postItView.findViewById<ImageButton>(R.id.add_fragment_button)
        val txtTittleClone = postItView.findViewById<EditText>(R.id.titleEditText)
        val txtContentClone = postItView.findViewById<EditText>(R.id.contentEditText)
        val btnDelete = postItView.findViewById<ImageButton>(R.id.btnTrash)

        colorButton.setOnClickListener {
            randomColor(postItView as RelativeLayout)
        }
        textColorButton.setOnClickListener {
            txtTittleClone.setTextColor(colorT)
            txtContentClone.setTextColor(colorT)

        }
        addButton.setOnClickListener {
            val title = txtTittleClone.text.toString().trim()
            val content = txtContentClone.text.toString().trim()
            val hexColor = String.format("#%06X", 0xFFFFFF and colorT)
            val id = dbHelper.insertPostIt(uniqueID,title, content, hexColor)
            dbHelper.close()
            addPostItLayout()

        }
        btnDelete.setOnClickListener(){
            dbHelper = DataBaseConnection(requireContext())
            val noteid = uniqueID
            Log.d("pepe", noteid.toString())
            dbHelper.deletePostItByTitle(txtTittleClone.text.toString().trim())
            containerPostIt.removeView(postItView)
        }

        containerPostIt.addView(postItView)
    }


}