package uk.ac.ed.inf.PizzaDronz.models;

public class Region {
    private String name = null;
    private LngLat[] vertices;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LngLat[] getVertices() {
        return vertices;
    }

    public boolean isValid(){
        if (vertices == null || vertices.length <= 2) {
            return false;
        }

        boolean allSameLng = true;
        boolean allSameLat = true;
        double firstLng = vertices[0].getLng();
        double firstLat = vertices[0].getLat();

        for (LngLat lngLat : vertices) {
            if (lngLat.getLng() != firstLng) {
                allSameLng = false;
            }
            if (lngLat.getLat() != firstLat) {
                allSameLat = false;
            }
        }

        // Region is invalid if all points have the same longitude or the same latitude
        return !(allSameLng || allSameLat);
    }
}
