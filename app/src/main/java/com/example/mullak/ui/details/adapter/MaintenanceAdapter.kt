package com.example.mullak.ui.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mullak.databinding.ItemMaintenanceRvBinding
import com.example.mullak.pojo.MaintenanceTypeService
import com.example.mullak.pojo.Service

class MaintenanceAdapter(
    val context: Context,
    var items: MutableList<MaintenanceTypeService>,
    val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<MaintenanceAdapter.ViewHolder>() {
    private val services: MutableMap<Int, Service> = mutableMapOf()
    fun changeData(newData: MutableList<MaintenanceTypeService>) {
        val oldData = items
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemsDiffCallback(
                oldData,
                newData
            )
        )
        items = newData
        diffResult.dispatchUpdatesTo(this)
    }


    inner class ViewHolder(val binding: ItemMaintenanceRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MaintenanceTypeService, position: Int) = with(itemView) {

            binding.maintenanceNameCheckBox.text = item.label
            if (item.pivot.additional_input != 0)
                binding.etAdditionalInput.visibility = View.GONE
            else
                binding.etAdditionalInput.visibility = View.VISIBLE


                binding.maintenanceNameCheckBox.setOnCheckedChangeListener { compoundButton, b ->
                    var additionalInfo =
                        if (item.pivot.additional_input == 0)
                            "0"
                        else
                            binding.etAdditionalInput.text.toString()

                    var visitsNum = binding.visitsNumTv.text.toString().toInt()
                    binding.btnInc.setOnClickListener {
                        visitsNum++
                        binding.visitsNumTv.text = visitsNum.toString()
                    }
                    binding.btnDec.setOnClickListener {
                        if (visitsNum > 1)
                            visitsNum--
                        binding.visitsNumTv.text = visitsNum.toString()
                    }


                    services[position] = Service(
                        item.pivot.additional_input,
                        additionalInfo,
                        item.id,
                        visitsNum,
                        item.pivot.maintenance_type_id
                    )
                    itemClickListener!!.onClick(services)
                }



        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMaintenanceRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items.get(position), position)

    class ItemsDiffCallback(
        private val oldData: MutableList<MaintenanceTypeService>,
        private val newData: MutableList<MaintenanceTypeService>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].id == newData[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldData.size
        }

        override fun getNewListSize(): Int {
            return newData.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition] == newData[newItemPosition]
        }

    }

    interface ItemClickListener {
        fun onClick(services: MutableMap<Int, Service>)
    }
}