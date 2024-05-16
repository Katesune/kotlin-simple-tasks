import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun main() {
    runBlocking {
        println("start")

        launch {
            val retrofitClient = RetrofitClient
            val queryResult = retrofitClient.createSeveralRequests()

            for (result in queryResult.result) {
                result.printRecipe()
            }

            println("done")
        }

        println("finish")
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://api.edamam.com/api/recipes/v2/"
    private const val APP_ID = "74587bcf"
    private const val APP_KEY = "08d57de4694f391f7b3130b6977330b5"

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build() ?: throw IllegalStateException("Failed to create a retrofit instance with the settings set")

    private val api = retrofit.create(Searchable::class.java)

    suspend fun createSeveralRequests(): QueryResult = coroutineScope {

        //val (query, mealType, diet, health, calories) = inputValue().split(",").map {it.trim()}
        val (query, mealType, diet, health, calories) =
            "eggs, dinner, high-protein, no-oil-added, 100-300".split(",").map {it.trim()}

        val responseBySimpleQuery = async {
            val response = api.search(query, APP_ID, APP_KEY).execute().body()
            println("fetched query by simple query")
            foundRecipes(response)[0]
        }

        val responseByMealType = async {
            val response = api.searchByMealType(query, APP_ID, APP_KEY, mealType).execute().body()
            delay(500L)
            println("fetched query by meal type")
            foundRecipes(response)[0]
        }

        val responseByDiet = async {
            val response = api.searchByDiet(query, APP_ID, APP_KEY, diet).execute().body()
            delay(300L)
            println("fetched query by diet")
            foundRecipes(response)[0]
        }

        val responseByHealth = async {
            val response = api.searchByHealth(query, APP_ID, APP_KEY, health).execute().body()
            println("fetched query by health")
            foundRecipes(response)[0]
        }

        val responseByCalories = async {
            val response = api.searchByCalories(query, APP_ID, APP_KEY, calories).execute().body()
            println("fetched query by calories")
            foundRecipes(response)[0]
        }

        QueryResult(
            listOf(
                responseBySimpleQuery.await(),
                responseByMealType.await(),
                responseByDiet.await(),
                responseByHealth.await(),
                responseByCalories.await()
            )
        )
    }

    val inputValue: () -> String = {
        readlnOrNull() ?: throw IllegalStateException("query must not be blank")
    }

    val foundRecipes: (Response?) -> List<Recipe> = { response ->
        checkNotNull(response)
        if (response.hits.isEmpty()) throw IllegalStateException("Recipes were not found on request")
        else Response.parseToRecipes(response.hits)
    }
}