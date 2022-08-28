package za.co.dvt.weatherapp.data

data class WeeklyWeatherDataResponse(
    val list: ArrayList<list>,
    val city: city
)

data class list(
    val dt: Int,
    val main: main,
    val weather: ArrayList<weather>,
    val clouds: clouds,
    val wind: wind,
    val dt_txt: String
)

data class main(
    var temp: Double,
    var temp_min: Double,
    var temp_max: Double,
)

data class wind(
    var speed: Double,
)

data class clouds(
    var all: Int
)

data class city(
    val id: Int,
    val name: String,
    val country: String
)

data class weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)