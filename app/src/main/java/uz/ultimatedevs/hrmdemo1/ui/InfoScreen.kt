package uz.ultimatedevs.hrmdemo1.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.ultimatedevs.hrmdemo1.R
import uz.ultimatedevs.hrmdemo1.data.ResultData
import uz.ultimatedevs.hrmdemo1.data.User
import uz.ultimatedevs.hrmdemo1.databinding.ScreenHomeBinding
import uz.ultimatedevs.hrmdemo1.databinding.ScreenInfoBinding
import uz.ultimatedevs.hrmdemo1.domain.Repository
import uz.ultimatedevs.hrmdemo1.ui.adapter.UsersAdapter
import uz.ultimatedevs.hrmdemo1.ui.adapter.WorkHistoryAdapter

class InfoScreen : Fragment(R.layout.screen_info) {

    private val repo = Repository()
    private val adapter = WorkHistoryAdapter()
    private val binding by viewBinding(ScreenInfoBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    private val args by navArgs<InfoScreenArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnBack.clicks().debounce(100).onEach {
            navController.navigateUp()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.rvWorkHistory.adapter = adapter
        binding.txtName.text = args.name

        repo.getUserWorkHistory(args.userId).onEach {
            when (it) {
                is ResultData.Success -> {
                    adapter.submitList(it.data)
                }
                is ResultData.Message -> {

                }
                is ResultData.Error -> {

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}