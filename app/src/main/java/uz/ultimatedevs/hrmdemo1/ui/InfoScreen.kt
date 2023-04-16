package uz.ultimatedevs.hrmdemo1.ui

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
import uz.ultimatedevs.hrmdemo1.data.WorkHour
import uz.ultimatedevs.hrmdemo1.databinding.ScreenInfoBinding
import uz.ultimatedevs.hrmdemo1.domain.Repository
import uz.ultimatedevs.hrmdemo1.ui.adapter.WorkHistoryAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime

class InfoScreen : Fragment(R.layout.screen_info) {

    private val repo = Repository()
    private val adapter = WorkHistoryAdapter()
    private val binding by viewBinding(ScreenInfoBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val works = mutableListOf<WorkHour>()

    private val args by navArgs<InfoScreenArgs>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnBack.clicks().debounce(100).onEach {
            navController.navigateUp()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.rvWorkHistory.adapter = adapter
        binding.txtName.text = args.name

        repo.getUserWorkHistory(args.userId).onEach {
            when (it) {
                is ResultData.Success -> {
                    works.clear()
                    works.addAll(it.data)
                    adapter.submitList(it.data)
                }

                is ResultData.Message -> {

                }

                is ResultData.Error -> {

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnCalculate.clicks().debounce(100).onEach {
            binding.containerPrice.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnOK.clicks().debounce(100).onEach {
            val price = binding.inputHoursPrice.text.toString().replaceFirst("^0+", "").toInt()
            var allHours = 0f

            works.forEach {

                val dateFormat = SimpleDateFormat("HH:mm")
                val date1 = dateFormat.parse(it.startHour)
                val date2 = dateFormat.parse(it.endHour)
                val duration = date2!!.time - date1!!.time

//                val startTime = LocalTime.parse(it.startHour)
//                val endTime = LocalTime.parse(it.endHour)
//                val duration = Duration.between(startTime, endTime)

                val oneH = duration / 3600000f

                Log.d("TTT", duration.toString())
                Log.d("TTT", oneH.toString())
                allHours += oneH
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Xodim maoshi")
            builder.setMessage("Ishlangan vaqt: $allHours\nHisoblangan mablag': ${allHours * price}")

            builder.setPositiveButton("Ok") { dialogInterface, i ->
                binding.containerPrice.visibility = View.GONE
                dialogInterface.dismiss()
            }
            builder.setCancelable(false)
            builder.create()
            builder.show()

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}