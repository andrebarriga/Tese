import javax.naming.Name;

public class Vehicle {

    private String Name;
    private int Axis;
    private float D_front_1axis;
    private float D_1axis_2axis;
    private float D_2axis_3axis;
    private float D_3axis_4axis;
    private float D_4axis_5axis;
    private float D_5axis_6axis;
    private float D_6axis_7axis;
    private float D_lastaxis_back;
    private float Height;
    private float Velocity;
    private float Time_to_previous_vehicle;

    public Vehicle(String Nome, float front_1axis, float _1axis_2axis, float _2axis_3axis, float _3axis_4axis, float _4axis_5axis, float _5axis_6axis, float _6axis_7axis, float lastaxis_back, float height, float velocity, float time_to_previous_vehicle){

        Name = Nome;
        D_front_1axis = front_1axis;
        D_1axis_2axis = _1axis_2axis;
        D_2axis_3axis = _2axis_3axis;
        D_3axis_4axis = _3axis_4axis;
        D_4axis_5axis = _4axis_5axis;
        D_5axis_6axis = _5axis_6axis;
        D_6axis_7axis = _6axis_7axis;
        D_lastaxis_back = lastaxis_back;
        Height = height;
        Velocity = velocity;
        Time_to_previous_vehicle = time_to_previous_vehicle;
        Axis = 2;

        if (_2axis_3axis>0){
                D_2axis_3axis = _2axis_3axis;
                Axis++;
            if (_3axis_4axis>0){
                    D_3axis_4axis = _3axis_4axis;
                    Axis++;
                if (_4axis_5axis>0){
                        D_4axis_5axis = _4axis_5axis;
                        Axis++;
                    if (_5axis_6axis>0){
                            D_5axis_6axis = _5axis_6axis;
                            Axis++;
                        if (_6axis_7axis>0){
                                D_6axis_7axis =_6axis_7axis;
                                Axis++;
                        }
                    }
                }
            }
        }
    }

    public String getName() {
        return Name;
    }

    public int getAxis() {
        return Axis;
    }

    public float getD_front_1axis() {
        return D_front_1axis;
    }

    public float getD_1axis_2axis() {
        return D_1axis_2axis;
    }

    public float getD_2axis_3axis() {
        return D_2axis_3axis;
    }

    public float getD_3axis_4axis() {
        return D_3axis_4axis;
    }

    public float getD_4axis_5axis() {
        return D_4axis_5axis;
    }

    public float getD_5axis_6axis() {
        return D_5axis_6axis;
    }

    public float getD_6axis_7axis() {
        return D_6axis_7axis;
    }

    public float getD_lastaxis_back() {
        return D_lastaxis_back;
    }

    public float getHeight() {
        return Height;
    }

    public float getVelocity() {
        return Velocity;
    }

    public float getTime_to_previous_vehicle() {
        return Time_to_previous_vehicle;
    }
}
