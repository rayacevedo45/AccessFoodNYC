
package nyc.c4q.take2.accessfood.yelp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Span {

    @SerializedName("latitude_delta")
    @Expose
    private Double latitudeDelta;
    @SerializedName("longitude_delta")
    @Expose
    private Double longitudeDelta;

    /**
     * 
     * @return
     *     The latitudeDelta
     */
    public Double getLatitudeDelta() {
        return latitudeDelta;
    }

    /**
     * 
     * @param latitudeDelta
     *     The latitude_delta
     */
    public void setLatitudeDelta(Double latitudeDelta) {
        this.latitudeDelta = latitudeDelta;
    }

    /**
     * 
     * @return
     *     The longitudeDelta
     */
    public Double getLongitudeDelta() {
        return longitudeDelta;
    }

    /**
     * 
     * @param longitudeDelta
     *     The longitude_delta
     */
    public void setLongitudeDelta(Double longitudeDelta) {
        this.longitudeDelta = longitudeDelta;
    }

}
