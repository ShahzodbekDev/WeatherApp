package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchWeatherData("Andijon")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchWeatherData(query!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }


    private fun fetchWeatherData(citiyName: String) {




        val retrofir = Retrofit.Builder()
           .addConverterFactory(GsonConverterFactory.create())
           .baseUrl("https://api.openweathermap.org/data/2.5/")
           .build().create(ApiInterface::class.java)
        val response = retrofir.getWeatherData(citiyName, "718850cb37ae2338817995b173158bb9", "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperatura = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val wind = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.sea_level
                    val condition = responseBody.weather.firstOrNull()?.main?: "Unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min


                    binding.temp.text = "$temperatura °C"
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.temp.text = "$temperatura °C"
                    binding.weather.text = condition
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "$wind m/s"
                    binding.sunRise.text = "${time(sunRise)}"
                    binding.sunSet.text = "${time(sunSet)}"
                    binding.sea.text = "$seaLevel hPa"
                    binding.conditions.text = condition
                    binding.citiyName.text = citiyName
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()


                    changeImagesAccordingToCondtion(condition)


                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeImagesAccordingToCondtion(conditions: String) {
        
        when (conditions) {
           "Clear sky", "Sunny" , "Clear" -> {
               binding.root.setBackgroundResource(R.drawable.sunny_background)
               binding.lottieAnimationView.setAnimation(R.raw.sun)
           }
           "Partly clouds", "Clouds" , "Overcast", "Mist", "Foggy" -> {
               binding.root.setBackgroundResource(R.drawable.colud_background)
               binding.lottieAnimationView.setAnimation(R.raw.cloud)
           }
           "Light rain", "Drizzle" , "Moderate rain", "Heavy rain" , "Showers" -> {
               binding.root.setBackgroundResource(R.drawable.rain_background)
               binding.lottieAnimationView.setAnimation(R.raw.rain)
           }
           "Light snow", "Blizzard" , "Moderate snow", "Heavy snow" , -> {
               binding.root.setBackgroundResource(R.drawable.snow_background)
               binding.lottieAnimationView.setAnimation(R.raw.snow)
           }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}

// new zealandN



