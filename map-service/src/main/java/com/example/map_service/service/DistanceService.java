package com.example.map_service.service;

import com.example.map_service.entity.DistanceRecord;
import com.example.map_service.Model.Address;
import com.example.map_service.entity.Warehouse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DistanceService {

    private static final String ACCESS_TOKEN = "pk.eyJ1IjoibGluaGVoZWhlaGVoIiwiYSI6ImNseGh5bXU0MjFrMzEybnByNnJ6ZzJhMXQifQ.Qn1-nmzhjfUMfajkJwT34Q";

    public DistanceRecord calculateShortestDistance(Address address, Warehouse warehouse) {
        DistanceRecord record = new DistanceRecord();
        try {
            String[] profiles = {"driving", "walking", "cycling"};
            double shortestDistance = Double.MAX_VALUE;
            String bestRoute = "";

            for (String profile : profiles) {
                String urlString = String.format("https://api.mapbox.com/directions/v5/mapbox/%s/%s,%s;%s,%s?access_token=%s&geometries=geojson&overview=full",
                        profile, address.getLongitude(), address.getLatitude(), warehouse.getLongitude(), warehouse.getLatitude(), ACCESS_TOKEN);

                RestTemplate restTemplate = new RestTemplate();
                String response = restTemplate.getForObject(urlString, String.class);

                JSONObject jsonResponse = new JSONObject(response);
                JSONArray routes = jsonResponse.getJSONArray("routes");

                if (routes.length() > 0) {
                    JSONObject routeObject = routes.getJSONObject(0);
                    double distance = routeObject.getDouble("distance") / 1000; // convert to km
                    distance = round(distance, 2); // round to 2 decimal places

                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        bestRoute = routeObject.getJSONObject("geometry").toString(); // Store the best route
                    }
                }
            }

            record.setUserId(address.getUserId());
            record.setReceiverName(address.getReceiverName());
            record.setProvinceCity(address.getProvinceCity());
            record.setDistrict(address.getDistrict());
            record.setWard(address.getWard());
            record.setStreet(address.getStreet());
            record.setOriginLatitude(address.getLatitude());
            record.setOriginLongitude(address.getLongitude());
            record.setWarehouseId(warehouse.getId());
            record.setWarehouseName(warehouse.getName());
            record.setWarehouseProvinceCity(warehouse.getProvinceCity());
            record.setWarehouseDistrict(warehouse.getDistrict());
            record.setWarehouseWard(warehouse.getWard());
            record.setDestinationLatitude(warehouse.getLatitude());
            record.setDestinationLongitude(warehouse.getLongitude());
            record.setDistance(shortestDistance);
            record.setRoute(bestRoute); // Set the shortest route
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return record; // Return the DistanceRecord
    }

    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Calculate the distance

        return round(distance, 2); // round to 2 decimal places
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
