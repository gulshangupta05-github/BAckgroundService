package `in`.surelocal.backgroundservice

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfo (
    @SerializedName("latlong")
    val latlong:String=""
    ) :Serializable{
}