package com.kisanhub.test.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.kisanhub.test.App
import com.kisanhub.test.R
import com.kisanhub.test.databinding.WeatherFragmentBinding
import com.kisanhub.test.models.Rainfall
import com.kisanhub.test.util.Util
import kotlinx.android.synthetic.main.weather_fragment.*
import java.util.*


class WeatherFragment : Fragment(), View.OnClickListener{


    private lateinit var charLayout: BarChart;
    private lateinit var weatherViewModel: WeatherViewModel;


    companion object {
        fun newInstance() = WeatherFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        var binding: WeatherFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false);

        init()

        val rootView = binding.root
        binding.viewModel = weatherViewModel
        binding.executePendingBindings()

        rootView.findViewById<Button>(R.id.btnFetchWeather).also {
            it.setOnClickListener(this)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun init() {
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    private fun initView(view: View?) {

        val locationAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.location_list))
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner!!.adapter = locationAdapter

        val metricAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.metric_list))
        metricAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metricSpinner!!.adapter = metricAdapter

        initChart(view)
    }

    private fun initChart(rootView: View?) {
        charLayout = rootView!!.findViewById(R.id.chartLayout)
        charLayout.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be drawn
        charLayout.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        charLayout.setPinchZoom(false)

        charLayout.setDrawBarShadow(false)
        charLayout.setDrawGridBackground(false)

        val xAxis = charLayout.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        charLayout.axisLeft.setDrawGridLines(false)

        charLayout.animateY(1500)

        chartLayout.visibility = View.GONE

    }

    private fun showMessage(message: String) {
        val view: View = activity!!.findViewById(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnFetchWeather -> {
                if(Util.isNetworkAvailable()) {
                    getChartData()
                } else {
                    // Check for local data is present and show otherwise show error message
                    val rainfallList = App.applicationContext().getWeatherData(locationSpinner.selectedItem.toString() + metricSpinner.selectedItem.toString());
                    if(rainfallList != null) {
                        showGraphicalData(rainfallList)
                    } else {
                        showMessage(getString(R.string.no_internet))
                        showGraphicalData(ArrayList<Rainfall>())
                    }
                }
            }
        }
    }

    private fun showGraphicalData(rainfallList: List<Rainfall>) {

        // create the data for graph
        val values = ArrayList<BarEntry>()
        val xAxisDAta = ArrayList<String>()
        for ((index, rainFall : Rainfall) in rainfallList.withIndex()) {
            values.add(BarEntry(index.toFloat(), rainFall.value.toFloat()))
            xAxisDAta.add(Util.getMonthString(rainFall.month).toString())
        }


        val barDataSet: BarDataSet

        if (charLayout.data != null && charLayout.data.dataSetCount > 0) {
            barDataSet = charLayout.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.label = metricSpinner.selectedItem.toString()
            barDataSet.values = values
            charLayout.data.notifyDataChanged()
            charLayout.notifyDataSetChanged()
        } else {
            barDataSet = BarDataSet(values, metricSpinner.selectedItem.toString())
            barDataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)

            val data = BarData(dataSets)
            charLayout.data = data
            charLayout.setFitBars(true)
        }

        charLayout.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisDAta) as ValueFormatter?;
        charLayout.invalidate()

    }

    private fun getChartData() {
        weatherViewModel.getWeatherData(locationSpinner.selectedItem.toString(), metricSpinner.selectedItem.toString())?.observe(this,
                Observer<List<Rainfall>> { weatherDataList ->
                    showGraphicalData(weatherDataList)
                })
    }
}

