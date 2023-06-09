package uz.ultimatedevs.hrmdemo1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.ultimatedevs.hrmdemo1.data.WorkHour
import uz.ultimatedevs.hrmdemo1.databinding.ItemWorkHistoryBinding

class WorkHistoryAdapter :
    ListAdapter<WorkHour, WorkHistoryAdapter.ViewHolder>(WorkHistoryDifUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkHistoryAdapter.ViewHolder {
        return ViewHolder(
            ItemWorkHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkHistoryAdapter.ViewHolder, position: Int) =
        holder.bind()

    inner class ViewHolder(private val binding: ItemWorkHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                val d = getItem(absoluteAdapterPosition)
                txtDate.text = d.date
                txtStartHour.text = d.startHour
                txtEndHour.text = d.endHour
            }
        }
    }

    private object WorkHistoryDifUtil : DiffUtil.ItemCallback<WorkHour>() {
        override fun areItemsTheSame(oldItem: WorkHour, newItem: WorkHour): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: WorkHour, newItem: WorkHour): Boolean {
            return true
        }

    }


}