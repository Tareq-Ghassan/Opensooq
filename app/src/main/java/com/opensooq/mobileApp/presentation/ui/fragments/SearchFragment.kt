package com.opensooq.mobileApp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.opensooq.mobileApp.databinding.FragmentSearchBinding

/**
 * The `SearchFragment` class represents the search screen in the app.
 * It uses view binding to interact with the UI elements defined in the `FragmentSearchBinding` class.
 */
class SearchFragment : Fragment() {

    // View binding instance. It is initialized in onCreateView and cleared in onDestroyView.
    private var _binding: FragmentSearchBinding? = null

    // A non-nullable reference to the binding, used to interact with UI elements.
    private val binding get() = _binding!!

    /**
     * Called to have the fragment instantiate its user interface view.
     * Here, we inflate the layout and initialize the view binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using the generated binding class
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }
    /**
     * Called when the view previously created by onCreateView is about to be destroyed.
     * Here, we clear the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference
        _binding = null
    }
}