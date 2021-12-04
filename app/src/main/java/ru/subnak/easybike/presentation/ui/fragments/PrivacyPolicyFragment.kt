package ru.subnak.easybike.presentation.ui.fragments

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import ru.subnak.easybike.R
import ru.subnak.easybike.databinding.FragmentPrivacyPolicyBinding
import ru.subnak.easybike.databinding.FragmentWelcomeBinding

class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding: FragmentPrivacyPolicyBinding
        get() = _binding ?: throw RuntimeException("FragmentPrivacyPolicyBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater,container,false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val text: String = getString(R.string.privacy_policy)
//        val styledText = Html.fromHtml(text, FROM_HTML_MODE_COMPACT)
//        binding.tvPrivacyPolicy.text = styledText
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}