package com.example.mullak.ui.home

import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mullak.R
import com.example.mullak.databinding.FragmentHomeBinding
import com.example.mullak.pojo.BuildingRequest
import com.example.mullak.pojo.BuildingResponse
import com.example.mullak.ui.map.MapsFragment
import com.example.mullak.utils.Resource
import com.google.android.gms.maps.MapFragment


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var checked: String
    private var unitNum: Int = 1
    private lateinit var response: BuildingResponse
    private lateinit var viewModel: HomeViewModel
    private var request: BuildingRequest = BuildingRequest()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        MapsFragment.onLocationDetect = object : LocationChange {
            override fun onChange(location: Location, address: String) {
                request.lat = location.latitude
                request.lng = location.longitude
                request.building_address = address
            }

        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        binding.unitNumTv.text = unitNum.toString()
        binding.btnIncrement.setOnClickListener {
            unitNum++
            binding.unitNumTv.text = unitNum.toString()
        }

        binding.btnDecrement.setOnClickListener {
            if (unitNum > 1)
                unitNum--

            binding.unitNumTv.text = unitNum.toString()
        }


        return view
    }

    private fun subscribeLiveData() {
        viewModel.data.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                }
                Resource.Status.SUCCESS -> {
                    response = it.data!!
                }
                Resource.Status.ERROR -> {
                }
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        subscribeLiveData()
        viewModel.getData()

        binding.radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                var building_type_id = 0
                var building_unit_id = 0

                if (checkedId == R.id.radio_building) {
                    checked = "Building"
                }
                if (checkedId == R.id.radio_compound) {
                    checked = "Compound"
                }

                for (i in response.building_types) {
                    if (checked == i.name) {
                        building_type_id = i.id
                        building_unit_id = i.building_type_units[0].id
                    }
                }

                request.building_type_id = building_type_id.toString()
                request.building_unit_id = building_unit_id
            }
        )

        binding.btnAdd.setOnClickListener {
            request.building_name = binding.etName.text.toString()
            request.building_qty = binding.unitNumTv.text.toString().toInt()

            if (TextUtils.isEmpty(binding.etName.text.toString())) {
                Toast.makeText(requireContext(), "please enter unit name", Toast.LENGTH_SHORT).show()
            } else {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(request, response)
                view.findNavController().navigate(action)
            }
        }

    }

    interface LocationChange {
        fun onChange(location: Location, address: String)
    }
}