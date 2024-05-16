import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers
import retrofit2.http.Query;

interface Searchable {

    @GET("/search")
    fun search (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String): Call<Response>

    @GET("/search")
    fun searchByMealType (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("mealType") mealType: String ): Call<Response>

    @GET("/search")
    fun searchByDiet (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("diet") diet: String ): Call<Response>

    @GET("/search")
    fun searchByHealth (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("health") health: String ): Call<Response>

    @GET("/search")
    fun searchByCalories (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("calories") calories: String ): Call<Response>

    @GET("/search")
    fun searchByTotalProps (
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("mealType") mealType: String,
        @Query("diet") diet: String,
        @Query("health") health: String,
        @Query("calories") calories: String
    ): Call<Response>
}

class QueryResult (
    val result: List<Recipe>,
//    val recipesBySimpleQuery: List<Recipe>,
//    val recipesByMealType: List<Recipe>,
//    val recipesByDiet: List<Recipe>,
//    val recipesByHealth: List<Recipe>,
//    val recipesByCalories: List<Recipe>,
)