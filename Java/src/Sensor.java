public class Sensor {

    private String Type;
    private float x_inicial;
    private float x_final;


    public Sensor(String type, float distance, float x){
        Type = type;
        x_inicial = distance;
        x_final = distance + x;
    }

    public String getType() {
        return Type;
    }

    public float getX_inicial() {
        return x_inicial;
    }

    public float getX_final() {
        return x_final;
    }
}
