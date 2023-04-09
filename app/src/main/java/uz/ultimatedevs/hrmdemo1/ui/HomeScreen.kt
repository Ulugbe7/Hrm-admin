package uz.ultimatedevs.hrmdemo1.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.ultimatedevs.hrmdemo1.R
import uz.ultimatedevs.hrmdemo1.data.ResultData
import uz.ultimatedevs.hrmdemo1.data.User
import uz.ultimatedevs.hrmdemo1.databinding.ScreenHomeBinding
import uz.ultimatedevs.hrmdemo1.domain.Repository
import uz.ultimatedevs.hrmdemo1.ui.adapter.UsersAdapter
import uz.ultimatedevs.hrmdemo1.ui.dialog.AddUserDialog

class HomeScreen : Fragment(R.layout.screen_home) {

    private val repo = Repository()
    private val usersAdapter = UsersAdapter()
    private var users = mutableListOf<User>()
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()

        repo.getUsers().onEach {
            when (it) {
                is ResultData.Success -> {
                    users = it.data as MutableList<User>
                    usersAdapter.submitList(users)
                }
                is ResultData.Message -> {

                }
                is ResultData.Error -> {

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initViews() {
        binding.apply {
            rvUsers.adapter = usersAdapter

            btnAddUser.clicks().debounce(100).onEach {
                val dialog = AddUserDialog(requireContext())
                dialog.setCanceledOnTouchOutside(false)
                dialog.setOnSaveClickListener {
                    repo.addUser(it).onEach { result ->
                        when (result) {
                            is ResultData.Success -> {
                                users.add(result.data)
                                usersAdapter.notifyItemInserted(users.size)
                            }
                            is ResultData.Message -> {

                            }
                            is ResultData.Error -> {

                            }
                        }
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                dialog.show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            usersAdapter.setOnClickListener {
                navController.navigate(
                    HomeScreenDirections.actionHomeScreenToInfoScreen(
                        it.name,
                        it.id
                    )
                )
            }

            usersAdapter.setOnDeleteClickListener { s, i ->

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Ogohlantirish")
                builder.setMessage("Rosttan ham o'chirmoqchimisiz?")
                builder.setCancelable(false)
                builder.setPositiveButton("Ha") { dialog, which ->
                    repo.deleteUser(s).onEach {
                        when (it) {
                            is ResultData.Success -> {
                                users.removeAt(i)
                                usersAdapter.notifyItemRemoved(i)
                            }
                            is ResultData.Message -> {

                            }
                            is ResultData.Error -> {

                            }
                        }
                        dialog.dismiss()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                builder.setNegativeButton("Yo'q") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

            usersAdapter.setOnEditClickListener { user, index ->
                val dialog = AddUserDialog(requireContext(), user)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setOnSaveClickListener {
                    repo.updateUser(it).onEach { result ->
                        when (result) {
                            is ResultData.Success -> {
                                users.removeAt(index)
                                users.add(index, it)
                                usersAdapter.notifyItemChanged(index)
                            }
                            is ResultData.Message -> {

                            }
                            is ResultData.Error -> {

                            }
                        }
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                dialog.show()
            }
        }

    }
}