package com.dinadurykina.eventtest.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.dinadurykina.eventtest.databinding.FragmentBinding
import com.dinadurykina.eventtest.util.observeEvent
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination
 */

class Fragment : Fragment() {

    private val viewModel: FragmentViewModel by viewModels()
    private lateinit var binding: FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewmodel = viewModel


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toast.observeEvent(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            binding.message.setText(viewModel.toast.value?.peekContent() ?: "nul")
        }

        viewModel.keyBoard.observeEvent(viewLifecycleOwner) {
            when (it) {
                true -> showKeyboard()
                false -> hideKeyboard()
            }
            binding.message.setText((viewModel.keyBoard.value?.peekContent() ?: "nul").toString())
        }
        viewModel.snackbar.observeEvent(viewLifecycleOwner) {
           val  snackbar = Snackbar.make(binding.root, it.toString(),
                Snackbar.LENGTH_LONG)
            snackbar.show()
            binding.message.setText( viewModel.snackbar.value?.peekContent()?:"nul")
        }
    }

    private fun showKeyboard () =
        binding.apply {
            message.requestFocus()
            val imm =
                ContextCompat.getSystemService(message.context, InputMethodManager::class.java)
            imm!!.showSoftInput(message, 0)
        }

    private fun hideKeyboard() =
        binding.apply {
            invalidateAll()   // обновить экран
            message.requestFocus()
            val imm =
                ContextCompat.getSystemService(message.context, InputMethodManager::class.java)
            imm!!.hideSoftInputFromWindow(message.windowToken, 0)
        }

}