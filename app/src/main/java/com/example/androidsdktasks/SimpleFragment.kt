package com.example.androidsdktasks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.androidsdktasks.databinding.FragmentSimpleBinding


class SimpleFragment : Fragment(R.layout.fragment_simple) {

    private var _binding: FragmentSimpleBinding? = null
    private val binding: FragmentSimpleBinding get() = _binding!!

    private var fragmentNumber = FRAGMENT_NUBMER_NOT_SET

    private val onBackPressedCallback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSimpleBinding.bind(view)

        fragmentNumber = if (savedInstanceState != null) {
             savedInstanceState.getInt(KEY_FRAGMENT_NUMBER)
        } else {
            arguments?.let {
                it.getInt(Router.KEY_FRAGMENT_NUMBER)
            } ?: FRAGMENT_NUBMER_NOT_SET
        }
        if (fragmentNumber == 1) {
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                onBackPressedCallback
            )
        }
        initializeView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_FRAGMENT_NUMBER, fragmentNumber)
    }

    private fun initializeView() {
        val activity = requireActivity() as MainActivity

        binding.fragmentNumber.text = "Fragment: $fragmentNumber"

        binding.prevScreenButton.apply {
            if (fragmentNumber > 1) {
                setOnClickListener { activity.router.popFragment() }
            } else {
                isEnabled = false
            }
        }
        binding.nextScreenButton.setOnClickListener {
            activity.router.openNextFragment(SimpleFragment())
        }
    }


    companion object {
        private const val KEY_FRAGMENT_NUMBER = "number"
        private const val FRAGMENT_NUBMER_NOT_SET = -1
    }
}