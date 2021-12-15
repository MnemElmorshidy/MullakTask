package com.example.mullak.ui.pdf

import android.R
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mullak.databinding.FragmentPDFBinding


class PDFFragment : Fragment() {

    private lateinit var binding: FragmentPDFBinding
    private val args by navArgs<PDFFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPDFBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.javaScriptEnabled = true
        val url = "https://docs.google.com/gview?embedded=true&url=${args.result.quotation}"
        binding.webView.loadUrl(url)

        binding.btnDownload.setOnClickListener {
            val request =
                DownloadManager.Request(Uri.parse(args.result.quotation))
            request.setTitle("munjiz.pdf")
                .setMimeType("application/pdf")
                .setAllowedOverMetered(true)
                .setDescription("File is downloading...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "munjiz pdf")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            val dm : DownloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE)  as DownloadManager
            dm.enqueue(request)
        }

        binding.btnShare.setOnClickListener {

            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, args.result.quotation)

            requireActivity()!!.startActivity(i)
        }

        return view
    }


}