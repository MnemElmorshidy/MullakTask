package com.example.mullak.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mullak.databinding.FragmentDetailsBinding
import com.example.mullak.pojo.BuildingRequest
import com.example.mullak.pojo.MaintenanceTypeService
import com.example.mullak.pojo.Service
import com.example.mullak.ui.details.adapter.MaintenanceAdapter
import com.example.mullak.utils.Resource


class DetailsFragment : Fragment(), MaintenanceAdapter.ItemClickListener {

    private lateinit var servicesMap: MutableMap<Int, Service>
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var maintenanceAdapter: MaintenanceAdapter
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var request: BuildingRequest
    private val args by navArgs<DetailsFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        request = args.request
        var data = mutableListOf<MaintenanceTypeService>()
        for (i in args.response.maintenance_types) {
            for (x in i.maintenance_type_services)
                data.add(x)
        }
        maintenanceAdapter = MaintenanceAdapter(requireContext(), data, this)
        binding.maintenanceRv.adapter = maintenanceAdapter

        binding.unitNameTv.text = request.building_name
        binding.unitsNumberTv.text = request.building_qty.toString()

        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeLiveData()


        binding.btnNext.setOnClickListener {
            var mutableList = mutableListOf<Service>()
            for (element in servicesMap)
                mutableList.add(element.value)

            detailsViewModel.addBuilding(request)

        }

    }

    private fun subscribeLiveData() {
        detailsViewModel.addBuildingLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                }
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        val action = DetailsFragmentDirections.actionDetailsFragmentToPDFFragment(it)
                        findNavController().navigate(action)
                    }
                }
                Resource.Status.ERROR -> {
                }
            }


        }
    }

    override fun onClick(services: MutableMap<Int, Service>) {
        this@DetailsFragment.servicesMap = services
    }

}