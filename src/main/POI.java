public class POI {
    private int ID;
    private double longitude, latitude, frequency;
    private String POI_name, POI_category_id, photos, POI;


    public POI(int ID, String POI, double latitude, double longitude, String photos, String POI_category_id, String POI_name) {
        this.ID = ID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.POI_name = POI_name;
        this.POI_category_id = POI_category_id;
        this.photos = photos;
        this.POI = POI;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPOI_name() {
        return POI_name;
    }

    public void setPOI_name(String POI_name) {
        this.POI_name = POI_name;
    }

    public String getPOI_category_id() {
        return POI_category_id;
    }

    public void setPOI_category_id(String POI_category_id) {
        this.POI_category_id = POI_category_id;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPOI() {
        return POI;
    }

    public void setPOI(String POI) {
        this.POI = POI;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
