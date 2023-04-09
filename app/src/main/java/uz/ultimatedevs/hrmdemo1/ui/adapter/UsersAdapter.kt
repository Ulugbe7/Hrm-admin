package uz.ultimatedevs.hrmdemo1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.ultimatedevs.hrmdemo1.data.User
import uz.ultimatedevs.hrmdemo1.databinding.ItemUserBinding

class UsersAdapter : ListAdapter<User, UsersAdapter.ViewHolder>(UsersDifUtil) {

    private var onEditClickListener: ((User, Int) -> Unit)? = null
    private var onDeleteClickListener: ((String, Int) -> Unit)? = null
    private var onClickListener: ((User) -> Unit)? = null

    fun setOnEditClickListener(block: (User, Int) -> Unit) {
        onEditClickListener = block
    }

    fun setOnDeleteClickListener(block: (String, Int) -> Unit) {
        onDeleteClickListener = block
    }

    fun setOnClickListener(block: (User) -> Unit) {
        onClickListener = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                val d = getItem(absoluteAdapterPosition)
                txtName.text = d.name
                txtProfession.text = d.profession

                root.setOnClickListener {
                    onClickListener?.invoke(d)
                }
                btnDelete.setOnClickListener {
                    onDeleteClickListener?.invoke(d.id, absoluteAdapterPosition)
                }
                btnEdit.setOnClickListener {
                    onEditClickListener?.invoke(d, absoluteAdapterPosition)
                }
            }
        }
    }

    private object UsersDifUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
                    || oldItem.login == newItem.login
                    || oldItem.password == newItem.password
                    || oldItem.profession == newItem.profession
        }
    }
}